package kr.co.hyo.api.member.controller.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

data class MemberRefreshTokenRequest(
    @field:NotBlank(message = "갱신 토큰을 입력하세요")
    @field:Schema(description = "갱신 토큰", example = "", required = true)
    val refreshToken: String,
)
