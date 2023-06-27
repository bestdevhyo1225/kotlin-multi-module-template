package kr.co.hyo.api.member.controller.request

import io.swagger.v3.oas.annotations.media.Schema
import kr.co.hyo.common.util.validator.Password

data class MemberChangePasswordRequest(
    @field:Password
    @field:Schema(description = "기존 비밀번호", example = "!@zdx90b8Cl3x2%&dz", required = true)
    val oldPassword: String,

    @field:Password
    @field:Schema(description = "변경할 비밀번호", example = "!@3d#Lew9%23Kz4kaC", required = true)
    val newPassword: String,
)
