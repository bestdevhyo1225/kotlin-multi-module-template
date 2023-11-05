package kr.co.hyo.domain.chatting.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class Chatting(
    val chattingRoomId: Long,
    val memberId: Long,
    val contents: String,
) {

    var createdAt: LocalDateTime = LocalDateTime.now()
        private set

    var updatedAt: LocalDateTime = LocalDateTime.now()
        private set

    var deletedAt: LocalDateTime? = null
        private set

    fun getChattingKey(): String = "chatting:room:${this.chattingRoomId}:messages"
}
