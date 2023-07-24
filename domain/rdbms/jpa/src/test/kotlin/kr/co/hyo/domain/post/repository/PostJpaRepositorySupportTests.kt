package kr.co.hyo.domain.post.repository

import jakarta.persistence.EntityManager
import kr.co.hyo.config.JpaConfig
import kr.co.hyo.domain.post.entity.Post
import kr.co.hyo.domain.post.repository.querydsl.PostJpaQueryDslRepositorySupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDateTime

@DataJpaTest
@ContextConfiguration(classes = [JpaConfig::class, PostJpaQueryDslRepositorySupport::class])
@DisplayName("PostJpaRepositorySupport 단위 테스트")
class PostJpaRepositorySupportTests {

    @Autowired
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var postJpaRepository: PostJpaRepository

    @Autowired
    lateinit var postJpaRepositorySupport: PostJpaRepositorySupport

    @Test
    fun `게시글 번호로 게시글을 조회한다`() {
        // given
        val post = Post(memberId = 1L, title = "title", contents = "contents")

        postJpaRepository.save(post)
        entityManager.flush()
        entityManager.clear()

        // when
        val findPost: Post = postJpaRepositorySupport.findById(id = post.id!!)

        // then
        assertThat(findPost).isEqualTo(post)
    }

    @Test
    fun `게시글 번호 리스트로 게시글 리스트를 조회한다`() {
        // given
        val memberId = 1L
        val posts: List<Post> = listOf(
            Post(memberId = memberId, title = "title1", contents = "contents1"),
            Post(memberId = memberId, title = "title2", contents = "contents2"),
            Post(memberId = memberId, title = "title3", contents = "contents3"),
            Post(memberId = memberId, title = "title4", contents = "contents4"),
            Post(memberId = memberId, title = "title5", contents = "contents5"),
        )

        postJpaRepository.saveAll(posts)
        entityManager.flush()
        entityManager.clear()

        // when
        val findPosts: List<Post> =
            postJpaRepositorySupport.findAllByIds(ids = posts.map { it.id!! })

        // then
        assertThat(findPosts).isNotEmpty
        assertThat(findPosts.size).isEqualTo(posts.size)
    }

    @Test
    fun `회원번호 리스트가 생성한 게시글 중에서 특정일 이후에 생성된 게시글 리스트를 조회한다`() {
        // given
        val posts: List<Post> = listOf(
            Post(memberId = 1L, title = "title1", contents = "contents1"),
            Post(memberId = 1L, title = "title2", contents = "contents2"),
            Post(memberId = 1L, title = "title3", contents = "contents3"),
            Post(memberId = 1L, title = "title4", contents = "contents4"),
            Post(memberId = 2L, title = "title5", contents = "contents5"),
            Post(memberId = 2L, title = "title6", contents = "contents6"),
            Post(memberId = 2L, title = "title7", contents = "contents7"),
            Post(memberId = 3L, title = "title8", contents = "contents8"),
            Post(memberId = 3L, title = "title9", contents = "contents9"),
            Post(memberId = 4L, title = "title10", contents = "contents10"),
            Post(memberId = 4L, title = "title11", contents = "contents11"),
            Post(memberId = 4L, title = "title12", contents = "contents12"),
            Post(memberId = 5L, title = "title13", contents = "contents13"),
        )

        postJpaRepository.saveAll(posts)
        entityManager.flush()
        entityManager.clear()

        // when
        val postIds: List<Long> = postJpaRepositorySupport.findIds(
            memberIds = listOf(1L, 2L, 3L, 4L, 5L),
            timelineUpdatedDatetime = LocalDateTime.now().minusDays(3),
            lastId = 0L,
            limit = 200L,
        )

        // then
        assertThat(postIds).isNotEmpty
        assertThat(postIds.size).isEqualTo(posts.size)
    }
}
