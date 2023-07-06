package kr.co.hyo.domain.reservation.entity

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@DisplayName("Reservation 단위 테스트")
class ReservationTests {

    @Test
    fun `예약을 생성한다`() {
        // given
        val type = "food"
        val nowDatetime = LocalDateTime.now()

        // when
        val reservation = Reservation(
            type = type,
            startDatetime = nowDatetime,
            endDatetime = nowDatetime.plusDays(14),
        )

        // then
        assertThat(reservation.type).isEqualTo(type)
        assertThat(reservation.startDatetime).isEqualTo(nowDatetime)
        assertThat(reservation.endDatetime).isEqualTo(nowDatetime.plusDays(14))
    }
}
