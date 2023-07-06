package kr.co.hyo.domain.reservation.dto

data class ReservationRequestCreateDto(
    val type: String,
    val memberId: Long,
)
