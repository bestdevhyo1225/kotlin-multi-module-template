package kr.co.hyo.domain.member.repository

import kr.co.hyo.domain.member.entity.Member

interface MemberRepositorySupport {
    fun find(id: Long): Member
    fun find(loginId: String): Member?
}
