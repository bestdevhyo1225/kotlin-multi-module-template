package kr.co.hyo.domain.chatting.service

import kr.co.hyo.domain.chatting.dto.ChattingRoomDto

interface ChattingRoomWriteService {
    fun create(createdBy: Long, name: String): ChattingRoomDto
    fun addMember(chattingRoomId: Long, memberId: Long)
}
