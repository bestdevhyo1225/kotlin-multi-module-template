package kr.co.hyo.domain.reservation.service.v1

import kr.co.hyo.domain.reservation.dto.ReservationCountDto
import kr.co.hyo.domain.reservation.entity.ReservationCount
import kr.co.hyo.domain.reservation.mapper.ReservationCountDtoMapper
import kr.co.hyo.domain.reservation.repository.ReservationCountRepository
import kr.co.hyo.domain.reservation.repository.ReservationCountRepositorySupport
import kr.co.hyo.domain.reservation.service.ReservationCountWriteService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ReservationCountWriteServiceV1(
    private val reservationCountRepository: ReservationCountRepository,
    private val reservationCountRepositorySupport: ReservationCountRepositorySupport,
) : ReservationCountWriteService {

    override fun createOrUpdateMaxNumber(reservationId: Long): ReservationCountDto {
        var reservationCount: ReservationCount? =
            reservationCountRepositorySupport.findByReservationIdAndPessimisticWriteLock(reservationId = reservationId)

        reservationCount?.let {
            it.incrementMaxNumber()
            return ReservationCountDtoMapper.toDto(reservationCount = it)
        }

        reservationCount = ReservationCount(reservationId = reservationId, maxNumber = 1)
        reservationCountRepository.save(reservationCount)
        return ReservationCountDtoMapper.toDto(reservationCount = reservationCount)
    }
}
