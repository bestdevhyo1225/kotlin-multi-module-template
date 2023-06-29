package kr.co.hyo.domain.member.dto

import java.time.LocalDate

data class MemberFollowDto(
    val followingId: Long,
    val followerId: Long,
    val createdDate: LocalDate,
)
