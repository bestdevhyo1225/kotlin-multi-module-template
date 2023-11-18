package kr.co.hyo.websocket.chatting.request

import kr.co.hyo.domain.chatting.dto.ChattingCreateDto

data class ChattingCreateRequest(
    val memberId: Long,
    val contents: String,
) {

    fun toDto(chattingRoomId: Long): ChattingCreateDto =
        ChattingCreateDto(chattingRoomId = chattingRoomId, memberId = memberId, contents = contents)
}
