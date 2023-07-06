package kr.co.hyo.domain.reservation.mapper

import kr.co.hyo.domain.reservation.dto.ReservationDto
import kr.co.hyo.domain.reservation.entity.Reservation

object ReservationDtoMapper {

    fun toDto(reservation: Reservation): ReservationDto =
        with(receiver = reservation) {
            ReservationDto(
                id = id!!,
                type = type,
                memberId = memberId,
                createdDate = createdDate,
                createdDatetime = createdDatetime,
            )
        }
}
