package kr.co.hyo.api.member.controller.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

data class MemberRegisterCoordinate(
    @field:NotNull(message = "위도를 입력하세요")
    @field:Schema(description = "위도", example = "33.5123", required = true)
    val latitude: Double,

    @field:NotNull(message = "경도를 입력하세요")
    @field:Schema(description = "경도", example = "-112.2693", required = true)
    val longitude: Double,
)
