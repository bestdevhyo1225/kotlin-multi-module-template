package kr.co.hyo.domain.reservation.service

import kr.co.hyo.domain.reservation.dto.ReservationRequestCreateDto

interface ReservationRequestWriteService {
    fun createReservationRequest(dto: ReservationRequestCreateDto): Boolean
}
