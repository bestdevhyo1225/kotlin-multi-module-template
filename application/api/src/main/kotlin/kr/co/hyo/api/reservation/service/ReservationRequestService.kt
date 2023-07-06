package kr.co.hyo.api.reservation.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.hyo.domain.reservation.dto.ReservationRequestCreateDto
import kr.co.hyo.domain.reservation.service.ReservationRequestWriteService
import kr.co.hyo.publisher.reservation.dto.ReservationRequestDto
import kr.co.hyo.publisher.reservation.producer.ReservationProducer
import org.springframework.stereotype.Service

@Service
class ReservationRequestService(
    private val reservationRequestWriteService: ReservationRequestWriteService,
    private val reservationProducer: ReservationProducer,
) {

    fun createReservation(dto: ReservationRequestCreateDto) {
        if (reservationRequestWriteService.create(dto = dto)) {
            CoroutineScope(context = Dispatchers.IO).launch { sendReservationRequestEvent(dto = dto) }
        }
    }

    private suspend fun sendReservationRequestEvent(dto: ReservationRequestCreateDto) {
        reservationProducer.sendAsync(with(receiver = dto) { ReservationRequestDto(type = type, memberId = memberId) })
    }
}
