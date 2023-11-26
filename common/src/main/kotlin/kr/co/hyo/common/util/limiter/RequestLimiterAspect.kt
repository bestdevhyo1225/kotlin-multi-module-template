package kr.co.hyo.common.util.limiter

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
//import io.github.bucket4j.Bucket
import kr.co.hyo.common.util.limiter.exception.RequestLimitExceededException
import mu.KotlinLogging
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import java.time.Duration.ofSeconds
import java.util.concurrent.atomic.AtomicLong

@Aspect
@Component
class RequestLimiterAspect {

    private val logger = KotlinLogging.logger {}

    private val requestLimitCounter: Cache<String, AtomicLong> = Caffeine.newBuilder()
        .maximumSize(10_000)
        .build()

//    private val bucket: Bucket = Bucket.builder()
//        .addLimit { bandwidthBuilderCapacityStage ->
//            // 용량이 20개 토큰이고, 재충전 속도가 6초당 1개 토큰인 버킷
//            bandwidthBuilderCapacityStage
//                .capacity(20)
//                .refillIntervally(10, ofSeconds(1))
//        }
//        .build()

    @Around("@annotation(requestLimiter)")
    fun checkAndIncrement(joinPoint: ProceedingJoinPoint, requestLimiter: RequestLimiter): Any {
        val methodSignature = joinPoint.signature as MethodSignature
        val methodName: String = methodSignature.declaringTypeName
        val currentLimitCount: AtomicLong = requestLimitCounter.getIfPresent(methodName) ?: AtomicLong(0)
        val limitCount: Long = requestLimiter.count

        if (currentLimitCount.get() >= limitCount) {
            throw RequestLimitExceededException(
                "Request limit exceeded (" +
                    "method: ${methodSignature.method.name}, " +
                    "current: $currentLimitCount, " +
                    "limit: $limitCount)"
            )
        }

        val incrementCurrentLimitCount = currentLimitCount.incrementAndGet()
        requestLimitCounter.put(methodName, currentLimitCount)

        logger.info {
            "Limit increased count (" +
                "method: ${methodSignature.method.name}, " +
                "current: ${incrementCurrentLimitCount}, " +
                "limit: $limitCount)"
        }

        return joinPoint.proceed()
    }

    // Exception 예외가 발생하는 경우에도 limit 카운터를 줄여줘야 하기때문에 After 에서 처리하도록
    @After("@annotation(requestLimiter)")
    fun decrement(joinPoint: JoinPoint, requestLimiter: RequestLimiter) {
        val methodSignature = joinPoint.signature as MethodSignature
        val methodName: String = methodSignature.declaringTypeName
        val currentLimitCount: AtomicLong = requestLimitCounter.getIfPresent(methodName) ?: AtomicLong(0)
        val limitCount: Long = requestLimiter.count

        if (currentLimitCount.get() > 0) {
            val decrementCurrentLimitCount = currentLimitCount.decrementAndGet()
            requestLimitCounter.put(methodName, currentLimitCount)

            logger.info {
                "Limit decreased count (" +
                    "method: ${methodSignature.method.name}, " +
                    "current: ${decrementCurrentLimitCount}, " +
                    "limit: $limitCount)"
            }
        }
    }
}
