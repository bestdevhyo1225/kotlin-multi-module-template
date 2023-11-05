package kr.co.hyo.domain.chatting.service.impl

import kr.co.hyo.domain.chatting.dto.ChattingCreateDto
import kr.co.hyo.domain.chatting.entity.Chatting
import kr.co.hyo.domain.chatting.entity.ChattingRoom
import kr.co.hyo.domain.chatting.mapper.ChattingEntityMapper
import kr.co.hyo.domain.chatting.repository.ChattingRedisTemplateRepository
import kr.co.hyo.domain.chatting.service.ChattingWriteService
import org.springframework.stereotype.Service

@Service
class ChattingWriteServiceImpl(
    private val chattingRedisTemplateRepository: ChattingRedisTemplateRepository,
) : ChattingWriteService {

    override fun saveMessage(dto: ChattingCreateDto) {
        chattingRedisTemplateRepository.get(
            key = ChattingRoom.getChattingRoomKey(id = dto.chattingRoomId),
            clazz = ChattingRoom::class.java,
        ) ?: throw NoSuchElementException("채팅방이 존재하지 않습니다.")

        val chatting: Chatting = ChattingEntityMapper.toEntity(dto = dto)

        chattingRedisTemplateRepository.lpush(key = chatting.getChattingKey(), value = chatting)
    }
}
