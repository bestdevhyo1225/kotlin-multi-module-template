package kr.co.hyo.domain.chatting.dto

import java.time.LocalDateTime

data class ChattingRoomDto(
    val id: Long,
    val createdBy: Long,
    val name: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
