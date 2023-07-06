package kr.co.hyo.consumer.reservation.request

data class ReservationRequest(
    val type: String,
    val memberId: Long,
)
