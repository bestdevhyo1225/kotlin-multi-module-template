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
    fun `게시글을 조회한다`() {
        // given
        val post = Post(memberId = 1L, title = "title", contents = "contents")

        postJpaRepository.save(post)
        entityManager.flush()
        entityManager.clear()

        // when
        val findPost: Post = postJpaRepositorySupport.findByMemberIdAndId(memberId = post.memberId, id = post.id!!)

        // then
        assertThat(findPost).isEqualTo(post)
    }
}
