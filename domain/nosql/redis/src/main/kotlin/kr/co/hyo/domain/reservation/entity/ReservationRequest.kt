package kr.co.hyo.domain.reservation.entity

import java.time.LocalDate

class ReservationRequest(
    private val type: String,
    private val date: LocalDate,
    private val memberId: Long,
) {

    operator fun component1(): String = "reservation:$type:date:$date:members"
    operator fun component2(): String = "reservation:$type:count"
    operator fun component3(): Long = memberId

    fun getReservationRequsetMembersKey(): String = component1()
    fun getReservationCountKey(): String = component2()
}
