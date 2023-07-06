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
        val name = "예약1"
        val totalQuantity = 50_000
        val nowDatetime = LocalDateTime.now()

        // when
        val reservation = Reservation(
            name = name,
            totalQuantity = totalQuantity,
            startDatetime = nowDatetime,
            endDatetime = nowDatetime.plusDays(14),
        )

        // then
        assertThat(reservation.name).isEqualTo(name)
        assertThat(reservation.totalQuantity).isEqualTo(totalQuantity)
        assertThat(reservation.startDatetime).isEqualTo(nowDatetime)
        assertThat(reservation.endDatetime).isEqualTo(nowDatetime.plusDays(14))
    }
}
