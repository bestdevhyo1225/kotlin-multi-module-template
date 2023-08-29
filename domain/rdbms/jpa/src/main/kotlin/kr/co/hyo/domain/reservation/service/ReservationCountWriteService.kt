package kr.co.hyo.domain.reservation.service

import kr.co.hyo.domain.reservation.dto.ReservationCountDto

interface ReservationCountWriteService {
    fun createOrUpdateMaxNumber(reservationId: Long): ReservationCountDto
}
