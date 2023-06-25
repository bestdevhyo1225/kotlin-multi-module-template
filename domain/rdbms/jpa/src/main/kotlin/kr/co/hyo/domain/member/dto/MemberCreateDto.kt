package kr.co.hyo.domain.member.dto

data class MemberCreateDto(
    val name: String,
    val loginId: String,
    val password: String,
    val email: String,
)
