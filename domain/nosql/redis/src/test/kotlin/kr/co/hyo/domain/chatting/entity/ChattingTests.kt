package kr.co.hyo.domain.chatting.entity

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Chatting 단위 테스트")
class ChattingTests {

    @Test
    fun `Chatting을 통해 Key를 생성한다`() {
        // given
        val chatting = Chatting(chattingRoomId = 1, memberId = 1, contents = "contents")

        // when
        val chattingKey = chatting.getChattingKey()

        // then
        assertThat(chattingKey).isEqualTo("chatting:room:1:messages")
    }
}
