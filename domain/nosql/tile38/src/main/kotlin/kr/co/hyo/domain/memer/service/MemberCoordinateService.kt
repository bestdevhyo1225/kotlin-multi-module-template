package kr.co.hyo.domain.memer.service

interface MemberCoordinateService {

    fun getCoordinate(memberId: Long)
    fun setCoordinate(memberId: Long, latitude: Double, longitude: Double)
}
