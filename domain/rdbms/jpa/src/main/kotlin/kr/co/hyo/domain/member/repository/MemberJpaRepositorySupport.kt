package kr.co.hyo.domain.member.repository

import kr.co.hyo.domain.member.entity.Member

interface MemberJpaRepositorySupport {
    fun findById(id: Long): Member
    fun findByLoginId(loginId: String): Member
}
