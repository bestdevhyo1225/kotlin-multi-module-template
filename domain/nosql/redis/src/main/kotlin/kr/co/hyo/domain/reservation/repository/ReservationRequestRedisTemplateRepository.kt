package kr.co.hyo.domain.reservation.repository

import kr.co.hyo.domain.reservation.entity.ReservationRequest

interface ReservationRequestRedisTemplateRepository {
    fun create(reservationRequest: ReservationRequest): Long
}
