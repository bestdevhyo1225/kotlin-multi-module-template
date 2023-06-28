package kr.co.hyo.domain.member.dto

data class MemberFollowDto(
    val memberId: Long,
    val followerId: Long,
)
