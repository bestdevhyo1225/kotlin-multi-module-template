package kr.co.hyo.domain.memer.repository

import kr.co.hyo.domain.memer.entity.MemberCoordinate

object MemberCoordinateQueries {
    fun pointQuery(coordinate: MemberCoordinate) = arrayOf("POINT", coordinate.latitude, coordinate.longitude)
    fun limitQuery(limit: Int) = arrayOf("LIMIT", limit)
}
