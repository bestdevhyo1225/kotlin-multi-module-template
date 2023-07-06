package kr.co.hyo.domain.reservation.dto

data class ReservationRequestCreateDto(
    val reservationId: Long,
    val totalQuantity: Int,
    val memberId: Long,
)
