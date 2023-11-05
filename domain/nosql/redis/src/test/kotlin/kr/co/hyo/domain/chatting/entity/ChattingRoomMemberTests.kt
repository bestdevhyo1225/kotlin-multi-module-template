package kr.co.hyo.domain.chatting.entity

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("ChattingRoomMember 단위 테스트")
class ChattingRoomMemberTests {

    @Test
    fun `Chatting을 통해 Key를 생성한다`() {
        // given
        val chattingRoomMember = ChattingRoomMember(chattingRoomId = 1, memberId = 1)

        // when
        val chattingRoomMemberKey = chattingRoomMember.getChattingRoomMemberKey()

        // then
        assertThat(chattingRoomMemberKey).isEqualTo("chatting:room:1:members")
    }
}
