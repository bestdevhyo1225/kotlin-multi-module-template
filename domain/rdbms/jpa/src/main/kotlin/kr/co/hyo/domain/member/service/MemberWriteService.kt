package kr.co.hyo.domain.member.service

import kr.co.hyo.domain.member.dto.MemberCreateDto
import kr.co.hyo.domain.member.dto.MemberDto

interface MemberWriteService {
    fun createMember(dto: MemberCreateDto): MemberDto
    fun changeMemberPassword(memberId: Long, oldPassword: String, newPassword: String)
    fun changeMemberEmail(memberId: Long, email: String)
    fun changeMemberTimelineUpdatedDatetime(memberId: Long)
}
