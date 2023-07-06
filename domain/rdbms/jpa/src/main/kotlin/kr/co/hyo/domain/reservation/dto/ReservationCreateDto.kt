package kr.co.hyo.domain.reservation.dto

data class ReservationCreateDto(
    val type: String,
    val memberId: Long,
)
