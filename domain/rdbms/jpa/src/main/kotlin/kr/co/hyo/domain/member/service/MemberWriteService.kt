package kr.co.hyo.domain.member.service

import kr.co.hyo.domain.member.dto.MemberCreateDto
import kr.co.hyo.domain.member.dto.MemberDto

interface MemberWriteService {
    fun createMember(dto: MemberCreateDto): MemberDto
    fun changePassword(memberId: Long, oldPassword: String, newPassword: String)
    fun changeEmail(memberId: Long, email: String)
    fun changeTimelineUpdatedDatetime(memberId: Long)
}
