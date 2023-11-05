package kr.co.hyo.domain.chatting.mapper

import kr.co.hyo.domain.chatting.dto.ChattingRoomDto
import kr.co.hyo.domain.chatting.entity.ChattingRoom

object ChattingRoomDtoMapper {

    fun toDto(chattingRoom: ChattingRoom): ChattingRoomDto =
        with(receiver = chattingRoom) {
            ChattingRoomDto(
                id = id,
                createdBy = createdBy,
                name = name,
                createdAt = createdAt,
                updatedAt = updatedAt,
            )
        }
}
