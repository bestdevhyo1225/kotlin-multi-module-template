package kr.co.hyo.domain.member.mapper

import kr.co.hyo.domain.member.dto.MemberFollowDto
import kr.co.hyo.domain.member.entity.MemberFollow

object MemberFollowDtoMapper {

    fun toDto(memberFollow: MemberFollow): MemberFollowDto =
        with(receiver = memberFollow) {
            MemberFollowDto(
                followingId = followingId,
                followerId = followerId,
                createdDate = createdDate,
            )
        }
}
