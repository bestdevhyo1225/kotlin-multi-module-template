package kr.co.hyo.domain.member.entity

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("MemberFollow 단위 테스트")
class MemberFollowTests {

    @Test
    fun `팔로우를 생성한다`() {
        // given
        val followingId = 1L
        val followerId = 2L

        // when
        val memberFollow = MemberFollow(followingId = followingId, followerId = followerId)

        // then
        assertThat(memberFollow.followingId).isEqualTo(followingId)
        assertThat(memberFollow.followerId).isEqualTo(followerId)
    }

    @Test
    fun `팔로우 생성시, 회원번호가 존재하지 않으면 예외를 던진다`() {
        // given
        val followingId = 2L
        val followerId = 2L

        // when, then
        assertThatThrownBy { MemberFollow(followingId = followingId, followerId = followerId) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("자기 자신을 팔로우 할 수 없습니다.")
    }
}
