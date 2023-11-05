package kr.co.hyo.domain.chatting.service

import kr.co.hyo.domain.chatting.dto.ChattingRoomDto

interface ChattingRoomReadService {
    fun findChattingRooms(page: Long, size: Long): Pair<List<ChattingRoomDto>, Long>
}
