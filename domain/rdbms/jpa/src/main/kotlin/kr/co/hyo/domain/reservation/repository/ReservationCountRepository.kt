package kr.co.hyo.domain.reservation.repository

import kr.co.hyo.domain.reservation.entity.ReservationCount
import org.springframework.data.jpa.repository.JpaRepository

interface ReservationCountRepository : JpaRepository<ReservationCount, Long>
