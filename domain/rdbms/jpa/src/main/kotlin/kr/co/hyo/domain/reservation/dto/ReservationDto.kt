package kr.co.hyo.domain.reservation.dto

import java.time.LocalDate
import java.time.LocalDateTime

data class ReservationDto(
    val id: Long,
    val type: String,
    val startDatetime: LocalDateTime,
    val endDatetime: LocalDateTime,
    val createdDate: LocalDate,
    val createdDatetime: LocalDateTime,
)
