package kr.co.hyo.domain.reservation.entity

enum class ReservationRequestState(val code: Long, val message: String) {
    FAILED(code = -2, message = "실패"),
    EXIT(code = -1, message = "종료"),
    READY(code = 1, message = "준비");
}
