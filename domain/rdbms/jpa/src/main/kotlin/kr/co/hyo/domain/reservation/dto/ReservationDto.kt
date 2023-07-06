package kr.co.hyo.domain.reservation.dto

import java.time.LocalDate
import java.time.LocalDateTime

data class ReservationDto(
    val id: Long,
    val type: String,
    val memberId: Long,
    val createdDate: LocalDate,
    val createdDatetime: LocalDateTime,
)
