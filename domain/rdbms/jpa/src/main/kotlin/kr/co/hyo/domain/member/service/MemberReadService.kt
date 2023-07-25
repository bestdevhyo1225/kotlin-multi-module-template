package kr.co.hyo.domain.member.service

import kr.co.hyo.domain.member.dto.MemberDto
import kr.co.hyo.domain.member.dto.MemberAuthDto

interface MemberReadService {
    fun findMember(memberId: Long): MemberDto
    fun isCanNotMemberFanoutMaxLimit(memberId: Long): Boolean
    fun verifyMember(loginId: String, password: String): MemberAuthDto
    fun verifyMember(memberId: Long): MemberAuthDto
}
