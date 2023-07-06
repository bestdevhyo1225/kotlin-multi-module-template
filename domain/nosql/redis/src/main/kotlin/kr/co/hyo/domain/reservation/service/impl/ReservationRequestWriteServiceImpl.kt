package kr.co.hyo.domain.reservation.service.impl

import kr.co.hyo.domain.reservation.dto.ReservationRequestCreateDto
import kr.co.hyo.domain.reservation.entity.ReservationRequest
import kr.co.hyo.domain.reservation.entity.ReservationRequestState.FAILED
import kr.co.hyo.domain.reservation.entity.ReservationRequestState.READY
import kr.co.hyo.domain.reservation.repository.ReservationRequestRedisTemplateRepository
import kr.co.hyo.domain.reservation.service.ReservationRequestWriteService
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ReservationRequestWriteServiceImpl(
    private val reservationRequestRedisTemplateRepository: ReservationRequestRedisTemplateRepository,
) : ReservationRequestWriteService {

    override fun create(dto: ReservationRequestCreateDto): Boolean {
        val reservationRequest: ReservationRequest = with(receiver = dto) {
            ReservationRequest(
                reservationId = reservationId,
                totalQuantity = totalQuantity,
                memberId = memberId,
                date = LocalDate.now(),
            )
        }
        return when (reservationRequestRedisTemplateRepository.create(reservationRequest = reservationRequest)) {
            in listOf(READY.code) -> true
            in listOf(FAILED.code, FAILED.code) -> false
            else -> false
        }
    }
}
