package kr.co.hyo.domain.member.repository

import jakarta.persistence.EntityManager
import kr.co.hyo.config.JpaConfig
import kr.co.hyo.domain.member.entity.Member
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
    lateinit var memberJpaRepository: MemberJpaRepository

    @Autowired
    lateinit var memberFollowJpaRepository: MemberFollowJpaRepository

    @Autowired
    lateinit var memberFollowJpaRepositorySupport: MemberFollowJpaRepositorySupport

    @Test
    fun `회원의 팔로우 회원번호를 조회한다`() {
        // given
        val member = Member(
            name = "장효석",
            loginId = "hyo1225",
            password = "!8fZc92$@9bamcf",
            email = "devhyo@gmail.com",
        )

        memberJpaRepository.save(member)
        entityManager.flush()
        entityManager.clear()

        val memberFollows: List<MemberFollow> = (2L..21L).map { MemberFollow(member = member, followerId = it) }

        memberFollowJpaRepository.saveAll(memberFollows)
        entityManager.flush()
        entityManager.clear()

        // when
        val findMemberFollows: List<MemberFollow> = memberFollowJpaRepositorySupport.findAllByMemberId(
            memberId = member.id!!,
            lastFollowerId = 0L,
            limit = 10L,
        )

        // then
        assertThat(findMemberFollows).isNotEmpty
        assertThat(findMemberFollows).hasSize(10)
        assertThat(findMemberFollows.last().followerId).isEqualTo(11L)
    }
}