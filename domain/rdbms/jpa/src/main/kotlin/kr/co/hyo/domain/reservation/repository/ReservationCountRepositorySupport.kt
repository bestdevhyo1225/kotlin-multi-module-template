package kr.co.hyo.domain.reservation.repository

import kr.co.hyo.domain.reservation.entity.ReservationCount

interface ReservationCountRepositorySupport {
    fun findByReservationIdAndPessimisticWriteLock(reservationId: Long): ReservationCount?
}
