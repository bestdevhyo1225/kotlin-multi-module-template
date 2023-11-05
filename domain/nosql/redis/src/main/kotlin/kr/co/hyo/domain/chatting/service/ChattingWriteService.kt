package kr.co.hyo.domain.chatting.service

import kr.co.hyo.domain.chatting.dto.ChattingCreateDto

interface ChattingWriteService {
    fun saveMessage(dto: ChattingCreateDto)
}
