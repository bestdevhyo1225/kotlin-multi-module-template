package kr.co.hyo.domain.member.dto

import java.time.LocalDateTime

data class MemberDto(
    val id: Long,
    val name: String,
    val loginId: String,
    val email: String,
    val followCount: Long,
    val followingCount: Long,
    val timelineUpdatedDatetime: LocalDateTime?,
)
