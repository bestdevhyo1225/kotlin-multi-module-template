package kr.co.hyo.domain.reservation.repository.redistemplate

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kr.co.hyo.domain.reservation.entity.ReservationRequest
import kr.co.hyo.domain.reservation.entity.ReservationRequestState.EXIT
import kr.co.hyo.domain.reservation.entity.ReservationRequestState.FAILED
import kr.co.hyo.domain.reservation.repository.ReservationRequestRedisTemplateRepository
import org.springframework.data.redis.connection.RedisConnection
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class ReservationRequestRedisTemplateRepositoryImpl(
    private val redisTemplate: RedisTemplate<String, String>,
) : ReservationRequestRedisTemplateRepository {

    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    override fun create(reservationRequest: ReservationRequest): Long {
        val (key: String, totalQuantity: Int, memberId: Long) = reservationRequest
        var result: Long = EXIT.code

        redisTemplate.execute { redisConnection: RedisConnection ->
            try {
                redisConnection.multi() // 트랜잭션 시작

                val currentTotalQuantity: Long = redisTemplate.opsForSet().size(key)
                    ?: throw RuntimeException("scard 명령 수행 후, NULL 값이 반환됨")

                if (currentTotalQuantity < totalQuantity) {
                    result = redisTemplate.opsForSet().add(key, jacksonObjectMapper.writeValueAsString(memberId))
                        ?: throw RuntimeException("sadd 명령 수행 후, NULL 값이 반환됨")
                }

//                if (currentTotalQuantity == 0L) {
//                    redisTemplate.expire(key, expirationTimeMs, MILLISECONDS)
//                }

                redisConnection.exec() // 트랜잭션 커밋
            } catch (exception: RuntimeException) { // RedisConnectionFailureException, QueryTimeoutException 포함
                redisConnection.discard() // 트랜잭션 롤백
                result = FAILED.code
            }
            result
        }

        return result
    }
}
