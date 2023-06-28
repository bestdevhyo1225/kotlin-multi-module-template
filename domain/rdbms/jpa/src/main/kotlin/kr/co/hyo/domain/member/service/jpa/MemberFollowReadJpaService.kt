package kr.co.hyo.domain.member.service.jpa

import kr.co.hyo.domain.member.dto.MemberFollowDto
import kr.co.hyo.domain.member.entity.MemberFollow
import kr.co.hyo.domain.member.repository.MemberFollowJpaRepositorySupport
import kr.co.hyo.domain.member.service.MemberFollowReadService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberFollowReadJpaService(
    private val memberFollowJpaRepositorySupport: MemberFollowJpaRepositorySupport,
) : MemberFollowReadService {

    override fun findFollowers(memberId: Long, lastFollowerId: Long): List<MemberFollowDto> {
        val limit: Long = 200
        val memberFollows: List<MemberFollow> = memberFollowJpaRepositorySupport.findAllByMemberId(
            memberId = memberId,
            lastFollowerId = lastFollowerId,
            limit = limit,
        )
        return memberFollows.map {
            with(receiver = it) {
                MemberFollowDto(memberId = member.id!!, followerId = followerId)
            }
        }
    }
}
