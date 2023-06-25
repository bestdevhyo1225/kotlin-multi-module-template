package kr.co.hyo.domain.member.dto

data class MemberVerifyDto(
    val id: Long,
    val jwtClaims: Map<String, Any>,
)
