package kr.co.hyo.domain.reservation.entity

import java.time.LocalDate

class ReservationRequest(
    private val reservationId: Long,
    private val date: LocalDate,
    private val totalQuantity: Int,
    private val memberId: Long,
) {

    operator fun component1(): String = "reservation:request:$reservationId:date:$date:members"
    operator fun component2(): Int = totalQuantity
    operator fun component3(): Long = memberId

    fun getKey(): String = component1()
}
