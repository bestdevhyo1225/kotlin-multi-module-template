package kr.co.hyo.domain.post.entity

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Post 단위 테스트")
class PostTests {

    @Test
    fun `게시글을 생성한다`() {
        // given
        val memberId = 1L
        val title = "title"
        val contents = "contents"

        // when
        val post = Post(memberId = memberId, title = title, contents = contents)

        // then
        assertThat(post.memberId).isEqualTo(memberId)
        assertThat(post.title).isEqualTo(title)
        assertThat(post.contents).isEqualTo(contents)
        assertThat(post.likeCount).isZero()
        assertThat(post.viewCount).isZero()
    }

    @Test
    fun `게시물의 좋아요를 증가시킨다`() {
        // given
        val post = Post(memberId = 1L, title = "title", contents = "contents")

        // when
        post.incrementLikeCount()

        // then
        assertThat(post.likeCount).isOne()
    }

    @Test
    fun `게시물의 좋아요를 감소시킨다`() {
        // given
        val post = Post(memberId = 1L, title = "title", contents = "contents")
        post.incrementLikeCount()

        // when
        post.decrementLikeCount()

        // then
        assertThat(post.likeCount).isZero()
    }

    @Test
    fun `게시물의 조회수를 증가시킨다`() {
        // given
        val post = Post(memberId = 1L, title = "title", contents = "contents")

        // when
        post.incrementViewCount()

        // then
        assertThat(post.viewCount).isOne()
    }
}
