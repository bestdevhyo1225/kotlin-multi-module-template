package kr.co.hyo.domain.reservation.repository

import jakarta.persistence.EntityManager
import kr.co.hyo.config.JpaConfig
import kr.co.hyo.domain.reservation.entity.Reservation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ContextConfiguration

@DataJpaTest
@ContextConfiguration(classes = [JpaConfig::class])
@DisplayName("ReservationJpaRepository 단위 테스트")
class ReservationJpaRepositoryTests {

    @Autowired
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var reservationJpaRepository: ReservationJpaRepository

    @Test
    fun `예약을 저장한다`() {
        // given
        val reservation = Reservation(type = "food", memberId = 1L)

        // when
        reservationJpaRepository.save(reservation)
        entityManager.flush()
        entityManager.clear()

        // then
        val findReservation: Reservation? = reservationJpaRepository.findByIdOrNull(id = reservation.id!!)

        assertThat(findReservation).isNotNull
        assertThat(findReservation).isEqualTo(reservation)
    }
}
