package kr.co.hyo.domain.memer.service

interface MemberCoordinateService {

    fun getCoordinate(id: Long)
    fun setgetCoordinate(id: Long, latitude: Double, longitude: Double)
}
