package kr.co.hyo.api.chatting.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

data class ChattingRoomCreateRequest(

    @field:NotBlank(message = "채팅방 이름을 입력하세요")
    @field:Schema(description = "채팅방 이름", example = "저녁 식사 모임", required = true)
    val name: String,
)
