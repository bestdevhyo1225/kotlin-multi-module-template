package kr.co.hyo.api.reservation.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirements
import io.swagger.v3.oas.annotations.tags.Tag
import kr.co.hyo.api.reservation.service.ReservationRequestService
import kr.co.hyo.domain.reservation.dto.ReservationCountDto
import kr.co.hyo.domain.reservation.dto.ReservationRequestCreateDto
import kr.co.hyo.domain.reservation.service.ReservationCountWriteService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/reservations")
@Tag(name = "예약", description = "API Document")
class ReservationController(
    private val reservationRequestService: ReservationRequestService,
    private val reservationCountWriteService: ReservationCountWriteService,
) {

    @PostMapping("/{type}")
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(description = "예약 등록")
    fun reservations(authentication: Authentication, @PathVariable type: String) {
        val memberId: Long = authentication.name.toLong()
        val dto = ReservationRequestCreateDto(type = type, memberId = memberId)
        reservationRequestService.createReservation(dto = dto)
    }

    @PostMapping("/{id}/max-number")
    @Operation(description = "예약 카운트에서 최대 번호 발급")
    @SecurityRequirements
    fun reservationsMaxNumber(@PathVariable id: Long): ResponseEntity<ReservationCountDto> {
        val reservationCountDto: ReservationCountDto =
            reservationCountWriteService.createOrUpdateMaxNumber(reservationId = id)
        return ResponseEntity.ok(reservationCountDto)
    }
}
