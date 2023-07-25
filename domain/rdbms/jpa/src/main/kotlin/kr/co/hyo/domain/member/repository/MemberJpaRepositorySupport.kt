package kr.co.hyo.domain.member.repository

import kr.co.hyo.domain.member.entity.Member

interface MemberJpaRepositorySupport {
    fun find(id: Long): Member
    fun find(loginId: String): Member?
}
