package kr.co.hyo.domain.member.service.jpa

import kr.co.hyo.domain.member.dto.MemberFollowDto
import kr.co.hyo.domain.member.entity.Member.Companion.MEMBER_FANOUT_MAX_LIMIT
import kr.co.hyo.domain.member.entity.MemberFollow
import kr.co.hyo.domain.member.mapper.MemberFollowDtoMapper
import kr.co.hyo.domain.member.repository.MemberFollowJpaRepositorySupport
import kr.co.hyo.domain.member.service.MemberFollowReadService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberFollowReadJpaService(
    private val memberFollowJpaRepositorySupport: MemberFollowJpaRepositorySupport,
) : MemberFollowReadService {

    override fun findFollowers(followingId: Long, lastFollowerId: Long): List<MemberFollowDto> {
        val limit: Long = 200
        val memberFollows: List<MemberFollow> = memberFollowJpaRepositorySupport.findAll(
            followingId = followingId,
            lastFollowerId = lastFollowerId,
            limit = limit,
        )
        return memberFollows.map { MemberFollowDtoMapper.toDto(memberFollow = it) }
    }

    override fun findFollowings(followerId: Long): List<MemberFollowDto> =
        memberFollowJpaRepositorySupport.findAll(followerId = followerId, followCount = MEMBER_FANOUT_MAX_LIMIT)
}
