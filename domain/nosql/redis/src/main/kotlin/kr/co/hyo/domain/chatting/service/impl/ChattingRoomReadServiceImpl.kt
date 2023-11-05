package kr.co.hyo.domain.chatting.service.impl

import kr.co.hyo.domain.chatting.dto.ChattingRoomDto
import kr.co.hyo.domain.chatting.entity.ChattingRoom
import kr.co.hyo.domain.chatting.mapper.ChattingRoomDtoMapper
import kr.co.hyo.domain.chatting.repository.ChattingRedisTemplateRepository
import kr.co.hyo.domain.chatting.service.ChattingRoomReadService
import org.springframework.stereotype.Service

@Service
class ChattingRoomReadServiceImpl(
    private val chattingRedisTemplateRepository: ChattingRedisTemplateRepository,
) : ChattingRoomReadService {

    override fun findChattingRooms(page: Long, size: Long): Pair<List<ChattingRoomDto>, Long> {
        val start: Long = if (page <= 0L) 0L else page.minus(1).times(size)
        val end: Long = start.plus(size)
        val chattingRoomIds: List<Long> = chattingRedisTemplateRepository.zrevRange(
            key = ChattingRoom.getChattingRoomListKey(),
            start = start,
            end = end,
            clazz = Long::class.java,
        )
        val chattingRoomIdTotalCount: Long =
            chattingRedisTemplateRepository.zcard(key = ChattingRoom.getChattingRoomListKey())
        val keys: List<String> = chattingRoomIds.map { ChattingRoom.getChattingRoomKey(id = it) }
        val chattingRooms: List<ChattingRoom> = chattingRedisTemplateRepository
            .mget(keys = keys, clazz = ChattingRoom::class.java)

        return Pair(
            first = chattingRooms.map { ChattingRoomDtoMapper.toDto(chattingRoom = it) },
            second = chattingRoomIdTotalCount,
        )
    }
}
