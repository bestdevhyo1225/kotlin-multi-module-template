package kr.co.hyo.api.chatting.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Positive

data class ChattingRoomMemberCreateRequest(

    @field:Positive(message = "회원 ID는 0보다 큰 값을 입력하세요")
    @field:Schema(description = "회원 ID", example = "1", required = true)
    val memberId: Long,
)
