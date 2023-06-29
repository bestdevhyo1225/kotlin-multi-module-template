package kr.co.hyo.domain.member.dto

data class MemberDto(
    val id: Long,
    val name: String,
    val loginId: String,
    val email: String,
    val followCount: Long,
    val followingCount: Long,
)
