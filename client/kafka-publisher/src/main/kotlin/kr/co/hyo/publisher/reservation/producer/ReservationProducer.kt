package kr.co.hyo.publisher.reservation.producer

interface ReservationProducer {
    fun <T : Any> sendAsync(event: T)
}
