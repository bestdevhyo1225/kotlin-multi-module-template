package kr.co.hyo.domain.chatting.mapper

import kr.co.hyo.domain.chatting.dto.ChattingCreateDto
import kr.co.hyo.domain.chatting.entity.Chatting

object ChattingEntityMapper {

    fun toEntity(dto: ChattingCreateDto): Chatting =
        with(receiver = dto) {
            Chatting(
                chattingRoomId = chattingRoomId,
                memberId = memberId,
                contents = contents,
            )
        }
}
