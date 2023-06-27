package kr.co.hyo.domain.member.dto

data class MemberAuthDto(
    val id: Long,
    val jwtClaims: Map<String, Any>,
)
