package kr.co.hyo.domain.member.repository

import kr.co.hyo.domain.member.dto.MemberFollowDto
import kr.co.hyo.domain.member.entity.MemberFollow

interface MemberFollowJpaRepositorySupport {
    fun findAllByFollowingId(followingId: Long, lastFollowerId: Long, limit: Long): List<MemberFollow>
    fun findAllByFollowerId(followerId: Long, followCount: Long): List<MemberFollowDto>
}
