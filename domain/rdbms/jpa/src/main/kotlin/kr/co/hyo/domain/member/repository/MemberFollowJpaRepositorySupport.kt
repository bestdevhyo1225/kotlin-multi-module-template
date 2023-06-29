package kr.co.hyo.domain.member.repository

import kr.co.hyo.domain.member.entity.MemberFollow

interface MemberFollowJpaRepositorySupport {
    fun findAllByFollowingId(followingId: Long, lastId: Long, limit: Long): List<MemberFollow>
}
