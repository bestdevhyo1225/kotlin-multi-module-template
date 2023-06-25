package kr.co.hyo.domain.member.repository

import kr.co.hyo.domain.member.entity.Member

interface MemberRepositorySupport {
    fun findById(id: Long): Member
    fun findByLoginId(loginId: String): Member
}
