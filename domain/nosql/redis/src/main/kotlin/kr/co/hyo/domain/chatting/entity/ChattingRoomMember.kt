package kr.co.hyo.domain.chatting.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class ChattingRoomMember(
    val chattingRoomId: Long,
    val memberId: Long,
) {

    var createdAt: LocalDateTime = LocalDateTime.now()
        private set

    var updatedAt: LocalDateTime = LocalDateTime.now()
        private set

    var deletedAt: LocalDateTime? = null
        private set

    fun getChattingRoomMemberKey(): String = "chatting:room:${this.chattingRoomId}:members"
    fun getChattingMemberRoomKey(): String = "chatting:member:${this.memberId}:rooms"
}
