package kr.co.hyo.domain.chatting.service.impl

import kr.co.hyo.domain.chatting.dto.ChattingRoomDto
import kr.co.hyo.domain.chatting.entity.ChattingRoom
import kr.co.hyo.domain.chatting.entity.ChattingRoom.Companion.ZSET_CHATTING_ROOM_MAX_LIMIT
import kr.co.hyo.domain.chatting.entity.ChattingRoomMember
import kr.co.hyo.domain.chatting.mapper.ChattingRoomDtoMapper
import kr.co.hyo.domain.chatting.repository.ChattingRedisTemplateRepository
import kr.co.hyo.domain.chatting.service.ChattingRoomWriteService
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.sql.Timestamp

@Service
class ChattingRoomWriteServiceImpl(
    private val redisTemplate: RedisTemplate<String, String>,
    private val chattingRedisTemplateRepository: ChattingRedisTemplateRepository,
) : ChattingRoomWriteService {

    override fun create(createdBy: Long, name: String): ChattingRoomDto {
        // 트랜잭션을 활용해서 id 생성
        val saveChattingRoom: ChattingRoom = chattingRedisTemplateRepository.executeWithTrascation {
            val id: Long = chattingRedisTemplateRepository.increment(key = ChattingRoom.getChattingRoomIdKey())
            val chattingRoom = ChattingRoom(id = id, createdBy = createdBy, name = name)

            // 채팅방 생성
            chattingRedisTemplateRepository.set(key = chattingRoom.getChattingRoomKey(), value = chattingRoom)

            chattingRoom
        } ?: throw RuntimeException("채팅방 생성 실패")

        // 채팅방 목록에 id 저장
        chattingRedisTemplateRepository.zadd(
            key = saveChattingRoom.getChattingRoomListKey(),
            value = saveChattingRoom.id,
            score = Timestamp.valueOf(saveChattingRoom.createdAt).time.toDouble(),
        )

        return ChattingRoomDtoMapper.toDto(chattingRoom = saveChattingRoom)
    }

    override fun addMember(chattingRoomId: Long, memberId: Long) {
        val chattingRoom: ChattingRoom = chattingRedisTemplateRepository.get(
            key = ChattingRoom.getChattingRoomKey(id = chattingRoomId),
            clazz = ChattingRoom::class.java,
        ) ?: throw NoSuchElementException("채팅방이 존재하지 않습니다.")

        val chattingRoomMember = ChattingRoomMember(chattingRoomId = chattingRoomId, memberId = memberId)

        // 채팅방에 memberId를 추가한다.
        chattingRedisTemplateRepository.sadd(
            key = chattingRoomMember.getChattingRoomMemberKey(),
            value = chattingRoomMember,
        )
        // 회원 채팅방 목록에 chattingRoomId를 추가한다.
        chattingRedisTemplateRepository.zadd(
            key = chattingRoomMember.getChattingMemberRoomKey(),
            value = chattingRoomMember.chattingRoomId,
            score = Timestamp.valueOf(chattingRoom.updatedAt).time.toDouble(),
        )
    }
}
