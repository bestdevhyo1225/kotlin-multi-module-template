package kr.co.hyo.domain.memer.entity

class MemberCoordinate(
    val memberId: Long,
    val latitude: Double,
    val longitude: Double,
) {

    val x: Double
        get() = longitude

    val y: Double
        get() = latitude
}
