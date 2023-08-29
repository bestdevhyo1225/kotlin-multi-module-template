package kr.co.hyo.domain.reservation.mapper

import kr.co.hyo.domain.reservation.dto.ReservationCountDto
import kr.co.hyo.domain.reservation.entity.ReservationCount

object ReservationCountDtoMapper {

    fun toDto(reservationCount: ReservationCount): ReservationCountDto =
        with(receiver = reservationCount) {
            ReservationCountDto(
                id = id!!,
                reservationId = reservationId,
                maxNumber = maxNumber,
                createdDatetime = createdDatetime,
                updatedDatetime = updatedDatetime,
            )
        }
}
