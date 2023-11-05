package kr.co.hyo.domain.chatting.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class ChattingRoom(
    val id: Long,
    val createdBy: Long,
    val name: String,
) {

    var createdAt: LocalDateTime = LocalDateTime.now()
        private set

    var updatedAt: LocalDateTime = LocalDateTime.now()
        private set

    var deletedAt: LocalDateTime? = null
        private set

    companion object {
        const val ZSET_CHATTING_ROOM_MAX_LIMIT = -1_001L

        fun getChattingRoomIdKey(): String = "chatting:room:id"
        fun getChattingRoomKey(id: Long): String = "chatting:room:$id"
        fun getChattingRoomListKey(): String = "chatting:room:list"
    }

    fun getExpirationTimeMs(): Long = 60 * 60 * 24 * 7L // 7일 (SECONDS 기준)
    fun getChattingRoomKey(): String = "chatting:room:${this.id}"
    fun getChattingRoomListKey(): String = "chatting:room:list"
}
