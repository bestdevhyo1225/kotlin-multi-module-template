package kr.co.hyo.domain.member.repository

import jakarta.persistence.EntityManager
import kr.co.hyo.config.JpaConfig
import kr.co.hyo.domain.member.entity.MemberFollow
import kr.co.hyo.domain.member.repository.querydsl.MemberFollowJpaQueryDslRepositorySupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration

@DataJpaTest
@ContextConfiguration(classes = [JpaConfig::class, MemberFollowJpaQueryDslRepositorySupport::class])
@DisplayName("MemberFollowJpaRepositorySupport 단위 테스트")
class MemberFollowJpaRepositorySupportTests {

    @Autowired
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var memberFollowJpaRepository: MemberFollowJpaRepository

    @Autowired
    lateinit var memberFollowJpaRepositorySupport: MemberFollowJpaRepositorySupport

    @Test
    fun `회원의 팔로우 회원번호를 조회한다`() {
        // given
        val followingId: Long = 1
        val memberFollows: List<MemberFollow> = (2L..21L).map {
            MemberFollow(followingId = followingId, followerId = it)
        }

        memberFollowJpaRepository.saveAll(memberFollows)
        entityManager.flush()
        entityManager.clear()

        // when
        val findMemberFollows: List<MemberFollow> = memberFollowJpaRepositorySupport.findAllByFollowingId(
            followingId = followingId,
            lastId = 0L,
            limit = 10L,
        )

        // then
        assertThat(findMemberFollows).isNotEmpty
        assertThat(findMemberFollows).hasSize(10)
        assertThat(findMemberFollows.last().followerId).isEqualTo(11L)
    }
}
