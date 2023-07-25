package kr.co.hyo.domain.reservation.service

import kr.co.hyo.domain.reservation.dto.ReservationCreateDto
import kr.co.hyo.domain.reservation.dto.ReservationDto

interface ReservationWriteService {
    fun createReservation(dto: ReservationCreateDto): ReservationDto
}
