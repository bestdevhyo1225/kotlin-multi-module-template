package kr.co.hyo.api.member.controller.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

data class MemberChangeEmailRequest(
    @field:NotBlank(message = "변경할 이메일을 입력하세요")
    @field:Schema(description = "변경할 이메일", example = "devhyo7@gmail.com", required = true)
    val email: String,
)
