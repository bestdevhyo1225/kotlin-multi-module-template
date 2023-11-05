package kr.co.hyo.domain.chatting.dto

data class ChattingCreateDto(
    val chattingRoomId: Long,
    val memberId: Long,
    val contents: String,
)
