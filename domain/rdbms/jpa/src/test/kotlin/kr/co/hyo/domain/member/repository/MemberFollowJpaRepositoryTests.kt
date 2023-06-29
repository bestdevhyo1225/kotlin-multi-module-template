package kr.co.hyo.domain.member.repository

import jakarta.persistence.EntityManager
import kr.co.hyo.config.JpaConfig
import kr.co.hyo.domain.member.entity.MemberFollow
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ContextConfiguration

@DataJpaTest
@ContextConfiguration(classes = [JpaConfig::class])
@DisplayName("MemberFollowJpaRepository 단위 테스트")
class MemberFollowJpaRepositoryTests {

    @Autowired
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var memberFollowJpaRepository: MemberFollowJpaRepository

    @Test
    fun `회원 팔로우를 저장한다`() {
        // given
        val followingId = 1L
        val followerId = 2L
        val memberFollow = MemberFollow(followingId = followingId, followerId = followerId)

        // when
        memberFollowJpaRepository.save(memberFollow)
        entityManager.flush()
        entityManager.clear()

        // then
        val findMemberFollow: MemberFollow? = memberFollowJpaRepository.findByIdOrNull(id = memberFollow.id)

        assertThat(findMemberFollow).isNotNull
        assertThat(findMemberFollow).isEqualTo(memberFollow)
    }
}
