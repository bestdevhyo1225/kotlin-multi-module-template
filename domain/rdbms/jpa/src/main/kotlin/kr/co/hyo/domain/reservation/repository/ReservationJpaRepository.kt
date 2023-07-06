package kr.co.hyo.domain.reservation.repository

import kr.co.hyo.domain.reservation.entity.Reservation
import org.springframework.data.jpa.repository.JpaRepository

interface ReservationJpaRepository : JpaRepository<Reservation, Long>
