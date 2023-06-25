package kr.co.hyo.domain.member.service

import kr.co.hyo.domain.member.dto.MemberDto
import kr.co.hyo.domain.member.dto.MemberVerifyDto

interface MemberReadService {
    fun find(id: Long): MemberDto
    fun verify(loginId: String, password: String): MemberVerifyDto
}
