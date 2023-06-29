package kr.co.hyo.domain.post.repository

import jakarta.persistence.EntityManager
import kr.co.hyo.config.JpaConfig
import kr.co.hyo.domain.post.entity.Post
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ContextConfiguration

@DataJpaTest
@ContextConfiguration(classes = [JpaConfig::class])
@DisplayName("PostJpaRepository 단위 테스트")
class PostJpaRepositoryTests {

    @Autowired
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var postJpaRepository: PostJpaRepository

    @Test
    fun `게시글을 저장한다`() {
        // given
        val post = Post(memberId = 1L, title = "title", contents = "contents")

        // when
        postJpaRepository.save(post)
        entityManager.flush()
        entityManager.clear()

        // then
        val findPost: Post? = postJpaRepository.findByIdOrNull(id = post.id!!)

        assertThat(findPost).isNotNull
        assertThat(findPost).isEqualTo(post)
    }
}
