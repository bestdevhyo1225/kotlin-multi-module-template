package kr.co.hyo.domain.reservation.repository.redistemplate

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kr.co.hyo.domain.reservation.entity.ReservationRequest
import kr.co.hyo.domain.reservation.entity.ReservationRequestState
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

    override fun create(reservationRequest: ReservationRequest): ReservationRequestState {
        val (reservationRequsetMembersKey: String, reservationCountKey: String, memberId: Long) = reservationRequest
        var status: ReservationRequestState = EXIT

        redisTemplate.execute { redisConnection: RedisConnection ->
            try {
                redisConnection.multi() // 트랜잭션 시작

                val totalReservationCount: Long = redisTemplate.opsForValue().get(reservationCountKey)
                    ?.let { jacksonObjectMapper.readValue(it, Long::class.java) }
                    ?: throw RuntimeException("get 명령 수행 후, NULL 값이 반환됨")

                val reservationRequestCount: Long = redisTemplate.opsForSet().size(reservationRequsetMembersKey)
                    ?: throw RuntimeException("scard 명령 수행 후, NULL 값이 반환됨")

                if (reservationRequestCount < totalReservationCount) {
                    val value: String = jacksonObjectMapper.writeValueAsString(memberId)
                    val result: Long = redisTemplate.opsForSet().add(reservationRequsetMembersKey, value)
                        ?: throw RuntimeException("sadd 명령 수행 후, NULL 값이 반환됨")

                    status = ReservationRequestState.convert(result = result)
                }

                redisConnection.exec() // 트랜잭션 커밋
            } catch (exception: RuntimeException) { // RedisConnectionFailureException, QueryTimeoutException 포함
                redisConnection.discard() // 트랜잭션 롤백
                status = FAILED
            }
            status
        }

        return status
    }
}
