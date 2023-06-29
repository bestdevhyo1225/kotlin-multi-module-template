package kr.co.hyo.domain.member.service

import kr.co.hyo.domain.member.dto.MemberDto
import kr.co.hyo.domain.member.dto.MemberAuthDto

interface MemberReadService {
    fun find(memberId: Long): MemberDto
    fun isCanNotFanoutMaxLimit(memberId: Long): Boolean
    fun verify(loginId: String, password: String): MemberAuthDto
    fun verify(memberId: Long): MemberAuthDto
}
