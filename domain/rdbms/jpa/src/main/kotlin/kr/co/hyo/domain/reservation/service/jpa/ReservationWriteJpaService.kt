package kr.co.hyo.domain.reservation.service.jpa

import kr.co.hyo.domain.reservation.dto.ReservationCreateDto
import kr.co.hyo.domain.reservation.dto.ReservationDto
import kr.co.hyo.domain.reservation.entity.Reservation
import kr.co.hyo.domain.reservation.mapper.ReservationDtoMapper
import kr.co.hyo.domain.reservation.mapper.ReservationEntityMapper
import kr.co.hyo.domain.reservation.repository.ReservationJpaRepository
import kr.co.hyo.domain.reservation.service.ReservationWriteService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ReservationWriteJpaService(
    private val reservationJpaRepository: ReservationJpaRepository,
) : ReservationWriteService {

    override fun createReservation(dto: ReservationCreateDto): ReservationDto {
        val reservation: Reservation = ReservationEntityMapper.toEntity(dto = dto)
        reservationJpaRepository.save(reservation)
        return ReservationDtoMapper.toDto(reservation = reservation)
    }
}
