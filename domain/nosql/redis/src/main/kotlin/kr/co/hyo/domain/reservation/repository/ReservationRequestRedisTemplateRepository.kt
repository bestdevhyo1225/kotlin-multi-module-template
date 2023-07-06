package kr.co.hyo.domain.reservation.repository

import kr.co.hyo.domain.reservation.entity.ReservationRequest
import kr.co.hyo.domain.reservation.entity.ReservationRequestState

interface ReservationRequestRedisTemplateRepository {
    fun create(reservationRequest: ReservationRequest): ReservationRequestState
}
