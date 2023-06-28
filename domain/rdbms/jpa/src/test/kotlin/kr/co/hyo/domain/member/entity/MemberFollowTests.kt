package kr.co.hyo.domain.member.entity

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("MemberFollow 단위 테스트")
class MemberFollowTests {

    @Test
    fun `팔로우 생성시, 회원번호가 존재하지 않으면 예외를 던진다`() {
        // given
        val member = Member(
            name = "장효석",
            loginId = "hyo1225",
            password = "!8fZc92$@9bamcf",
            email = "devhyo@gmail.com",
        )
        val followerId = 2L

        // when, then
        assertThatThrownBy { MemberFollow(member = member, followerId = followerId) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("팔로우 할 회원번호가 null 입니다.")
    }
}
