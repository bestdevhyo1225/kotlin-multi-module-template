package kr.co.hyo.domain.reservation.entity

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Reservation 단위 테스트")
class ReservationTests {

    @Test
    fun `예약을 생성한다`() {
        // given
        val type = "food"
        val memberId = 1L

        // when
        val reservation = Reservation(type = type, memberId = memberId)

        // then
        assertThat(reservation.type).isEqualTo(type)
        assertThat(reservation.memberId).isEqualTo(memberId)
    }
}
