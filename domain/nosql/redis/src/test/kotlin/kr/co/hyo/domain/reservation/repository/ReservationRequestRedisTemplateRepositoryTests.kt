package kr.co.hyo.domain.reservation.repository

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kr.co.hyo.config.RedisEmbbededConfig
import kr.co.hyo.domain.reservation.entity.ReservationRequest
import kr.co.hyo.domain.reservation.entity.ReservationRequestState
import kr.co.hyo.domain.reservation.entity.ReservationRequestState.COMPLETED
import kr.co.hyo.domain.reservation.entity.ReservationRequestState.READY
import kr.co.hyo.domain.reservation.repository.redistemplate.ReservationRequestRedisTemplateRepositoryImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDate

@DataRedisTest(properties = ["spring.profiles.active=test"])
@DirtiesContext
@EnableAutoConfiguration
@ContextConfiguration(classes = [RedisEmbbededConfig::class, ReservationRequestRedisTemplateRepositoryImpl::class])
@DisplayName("ReservationRequestRedisTemplateRepository 단위 테스트")
class ReservationRequestRedisTemplateRepositoryTests {

    @Autowired
    lateinit var reservationRequestRedisTemplateRepository: ReservationRequestRedisTemplateRepository

    @Autowired
    lateinit var redisTemplate: RedisTemplate<String, String>

    @AfterEach
    fun tearDown() {
        redisTemplate.delete(redisTemplate.keys("*"))
    }

    @Test
    fun `예약 요청을 저장한다`() {
        // given
        val memberId = 1892741L
        val reservationCount = 50_000L
        val reservationRequest = ReservationRequest(
            type = "food",
            memberId = memberId,
            date = LocalDate.now(),
        )
        redisTemplate
            .opsForValue()
            .set(
                reservationRequest.getReservationCountKey(),
                jacksonObjectMapper().writeValueAsString(reservationCount),
            )

        // when
        val status: ReservationRequestState =
            reservationRequestRedisTemplateRepository.create(reservationRequest = reservationRequest)

        // then
        val size: Long? = redisTemplate
            .opsForSet()
            .size(reservationRequest.getReservationRequsetMembersKey())

        val isMember: Boolean? = redisTemplate
            .opsForSet()
            .isMember(
                reservationRequest.getReservationRequsetMembersKey(),
                jacksonObjectMapper().writeValueAsString(memberId),
            )

        assertThat(status).isEqualTo(READY)
        assertThat(size).isNotNull
        assertThat(size).isEqualTo(1L)
        assertThat(isMember).isNotNull
        assertThat(isMember).isTrue
    }

    @Test
    fun `이미 등록된 회원이 예약 요청을 한다`() {
        // given
        val memberId = 1892741L
        val reservationCount = 50_000L
        val reservationRequest = ReservationRequest(
            type = "food",
            memberId = memberId,
            date = LocalDate.now(),
        )
        redisTemplate
            .opsForValue()
            .set(
                reservationRequest.getReservationCountKey(),
                jacksonObjectMapper().writeValueAsString(reservationCount),
            )

        reservationRequestRedisTemplateRepository.create(reservationRequest = reservationRequest)

        // when
        val status: ReservationRequestState =
            reservationRequestRedisTemplateRepository.create(reservationRequest = reservationRequest)

        // then
        val size: Long? = redisTemplate
            .opsForSet()
            .size(reservationRequest.getReservationRequsetMembersKey())

        val isMember: Boolean? = redisTemplate
            .opsForSet()
            .isMember(
                reservationRequest.getReservationRequsetMembersKey(),
                jacksonObjectMapper().writeValueAsString(memberId),
            )

        assertThat(status).isEqualTo(COMPLETED)
        assertThat(size).isNotNull
        assertThat(size).isEqualTo(1L)
        assertThat(isMember).isNotNull
        assertThat(isMember).isTrue
    }
}
