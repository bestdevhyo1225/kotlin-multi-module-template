package kr.co.hyo.domain.member.repository

import kr.co.hyo.domain.member.dto.MemberFollowDto
import kr.co.hyo.domain.member.entity.MemberFollow

interface MemberFollowJpaRepositorySupport {
    fun findAll(followingId: Long, lastFollowerId: Long, limit: Long): List<MemberFollow>
    fun findAll(followerId: Long, followCount: Long): List<MemberFollowDto>
}
