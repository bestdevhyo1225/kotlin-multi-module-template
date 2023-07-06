package kr.co.hyo.domain.reservation.dto

import java.time.LocalDateTime

data class ReservationCreateDto(
    val name: String,
    val startDatetime: LocalDateTime,
    val endDatetime: LocalDateTime,
)
