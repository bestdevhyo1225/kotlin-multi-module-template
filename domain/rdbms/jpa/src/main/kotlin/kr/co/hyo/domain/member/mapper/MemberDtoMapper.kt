package kr.co.hyo.domain.member.mapper

import kr.co.hyo.domain.member.dto.MemberDto
import kr.co.hyo.domain.member.entity.Member

object MemberDtoMapper {

    fun toDto(member: Member): MemberDto =
        with(receiver = member) {
            MemberDto(
                id = id!!,
                name = name,
                loginId = loginId,
                email = email,
                followCount = followCount,
                followingCount = followingCount,
                timelineUpdatedDatetime = timelineUpdatedDatetime,
            )
        }
}
