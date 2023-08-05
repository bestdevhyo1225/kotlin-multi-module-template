package kr.co.hyo.domain.member.service.v1

import kr.co.hyo.domain.member.dto.MemberFollowDto
import kr.co.hyo.domain.member.entity.Member.Companion.MEMBER_FANOUT_MAX_LIMIT
import kr.co.hyo.domain.member.entity.MemberFollow
import kr.co.hyo.domain.member.mapper.MemberFollowDtoMapper
import kr.co.hyo.domain.member.repository.MemberFollowRepositorySupport
import kr.co.hyo.domain.member.service.MemberFollowReadService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberFollowReadServiceV1(
    private val memberFollowRepositorySupport: MemberFollowRepositorySupport,
) : MemberFollowReadService {

    override fun findFollowers(followingId: Long, lastFollowerId: Long): List<MemberFollowDto> {
        val limit: Long = 200
        val memberFollows: List<MemberFollow> = memberFollowRepositorySupport.findAll(
            followingId = followingId,
            lastFollowerId = lastFollowerId,
            limit = limit,
        )
        return memberFollows.map { MemberFollowDtoMapper.toDto(memberFollow = it) }
    }

    override fun findFollowings(followerId: Long, limit: Long): List<MemberFollowDto> =
        memberFollowRepositorySupport.findAllWithInnerJoin(
            followerId = followerId,
            followCount = MEMBER_FANOUT_MAX_LIMIT,
            limit = limit,
        )
}
