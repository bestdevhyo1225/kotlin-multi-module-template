package kr.co.hyo.domain.reservation.service.impl

import kr.co.hyo.domain.reservation.dto.ReservationRequestCreateDto
import kr.co.hyo.domain.reservation.entity.ReservationRequest
import kr.co.hyo.domain.reservation.entity.ReservationRequestState.COMPLETED
import kr.co.hyo.domain.reservation.entity.ReservationRequestState.EXIT
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

    override fun createReservationRequest(dto: ReservationRequestCreateDto): Boolean {
        val reservationRequest: ReservationRequest =
            with(receiver = dto) { ReservationRequest(type = type, memberId = memberId, date = LocalDate.now()) }
        return when (reservationRequestRedisTemplateRepository.create(reservationRequest = reservationRequest)) {
            in listOf(READY) -> true
            in listOf(FAILED, EXIT, COMPLETED) -> false // 'COMPLETED' 는 이미 등록된 회원이 있을 경우를 의미한다.
            else -> false
        }
    }
}
