package kr.co.hyo.domain.chatting.entity

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("ChattingRoom 단위 테스트")
class ChattingRoomTests {

    @Test
    fun `Chatting을 통해 Key를 생성한다`() {
        // given
        val chattingRoom = ChattingRoom(id = 1, createdBy = 1, name = "저녁 식사 모임")

        // when
        val chattingRoomKey = chattingRoom.getChattingRoomKey()

        // then
        assertThat(chattingRoomKey).isEqualTo("chatting:room:1")
    }
}
