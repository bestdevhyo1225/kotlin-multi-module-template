package kr.co.hyo.domain.reservation.mapper

import kr.co.hyo.domain.reservation.dto.ReservationCreateDto
import kr.co.hyo.domain.reservation.entity.Reservation

object ReservationEntityMapper {

    fun toEntity(dto: ReservationCreateDto): Reservation =
        with(receiver = dto) {
            Reservation(
                type = type,
                startDatetime = startDatetime,
                endDatetime = endDatetime,
            )
        }
}
