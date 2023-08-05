package kr.co.hyo.domain.member.repository

import kr.co.hyo.domain.member.dto.MemberFollowDto
import kr.co.hyo.domain.member.entity.MemberFollow

interface MemberFollowRepositorySupport {
    fun findAll(followingId: Long, lastFollowerId: Long, limit: Long): List<MemberFollow>
    fun findAllWithInnerJoin(followerId: Long, followCount: Long, limit: Long): List<MemberFollowDto>
}
