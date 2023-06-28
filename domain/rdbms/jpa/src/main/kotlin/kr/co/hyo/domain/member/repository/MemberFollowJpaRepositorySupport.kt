package kr.co.hyo.domain.member.repository

import kr.co.hyo.domain.member.entity.MemberFollow

interface MemberFollowJpaRepositorySupport {
    fun findAllByMemberId(memberId: Long, lastFollowerId: Long, limit: Long): List<MemberFollow>
}
