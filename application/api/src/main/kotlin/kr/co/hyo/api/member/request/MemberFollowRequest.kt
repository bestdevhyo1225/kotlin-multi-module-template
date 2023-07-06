package kr.co.hyo.api.member.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Positive

data class MemberFollowRequest(
    @field:Positive(message = "팔로우하는 회원번호는 0보다 커야합니다")
    @field:Schema(description = "팔로우하는 회원번호", example = "2", required = true)
    val followingId: Long,
)
