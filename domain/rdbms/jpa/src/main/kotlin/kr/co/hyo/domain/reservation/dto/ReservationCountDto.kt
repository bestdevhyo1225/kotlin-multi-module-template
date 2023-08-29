package kr.co.hyo.domain.reservation.dto

import java.time.LocalDateTime

data class ReservationCountDto(
    val id: Long,
    val reservationId: Long,
    val maxNumber: Int,
    val createdDatetime: LocalDateTime,
    val updatedDatetime: LocalDateTime,
)
