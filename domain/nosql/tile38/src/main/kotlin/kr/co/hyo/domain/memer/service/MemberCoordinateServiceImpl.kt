package kr.co.hyo.domain.memer.service

import kr.co.hyo.config.Tile38Keys
import kr.co.hyo.domain.memer.entity.MemberCoordinate
import kr.co.hyo.domain.memer.repository.MemberCoordinateQueries
import kr.co.hyo.domain.memer.repository.MemberCoordinateTile38Repository
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class MemberCoordinateServiceImpl(
    private val memberCoordinateTile38Repository: MemberCoordinateTile38Repository,
) : MemberCoordinateService {

    private val kotlinLogger = KotlinLogging.logger {}

    override fun getCoordinate(memberId: Long) {
        val result: String = memberCoordinateTile38Repository.get(Tile38Keys.PERSON, memberId.toString())
        kotlinLogger.info { "result: $result" }
    }

    override fun setCoordinate(memberId: Long, latitude: Double, longitude: Double) {
        val memberCoordinate = MemberCoordinate(memberId = memberId, latitude = latitude, longitude = longitude)
        memberCoordinateTile38Repository.set(
            Tile38Keys.PERSON,
            memberCoordinate.memberId,
            *MemberCoordinateQueries.pointQuery(coordinate = memberCoordinate)
        )
    }
}
