package kr.co.hyo.domain.member.repository

import jakarta.persistence.EntityManager
import kr.co.hyo.config.JpaConfig
import kr.co.hyo.domain.member.dto.MemberFollowDto
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
        val followingId: Long = 1
        val memberFollows: List<MemberFollow> = (2L..21L).map {
            MemberFollow(followingId = followingId, followerId = it)
        }

        memberFollowJpaRepository.saveAll(memberFollows)
        entityManager.flush()
        entityManager.clear()

        // when
        val findMemberFollows: List<MemberFollow> = memberFollowJpaRepositorySupport.findAll(
            followingId = followingId,
            lastFollowerId = 0L,
            limit = 10L,
        )

        // then
        assertThat(findMemberFollows).isNotEmpty
        assertThat(findMemberFollows).hasSize(10)
        assertThat(findMemberFollows.last().followerId).isEqualTo(11L)
    }

    @Test
    fun `회원이 팔로우하고 있는 팔로잉 회원 리스트를 조회한다`() {
        // given
        val followerMember = Member(
            name = "장효석",
            loginId = "hyo1234",
            password = "!8fZc92$@9bamcf",
            email = "devhyo@gmail.com",
        )
        val followingMembers: List<Member> = listOf(
            Member(
                name = "홍길동",
                loginId = "hong1234",
                password = "!8fZc92$@9bamcf",
                email = "devhyo@gmail.com",
            ),
            Member(
                name = "김영희",
                loginId = "hee1234",
                password = "!8fZc92$@9bamcf",
                email = "devhyo@gmail.com",
            ),
            Member(
                name = "유준수",
                loginId = "jun1234",
                password = "!8fZc92$@9bamcf",
                email = "devhyo@gmail.com",
            ),
            Member(
                name = "김준영",
                loginId = "young1234",
                password = "!8fZc92$@9bamcf",
                email = "devhyo@gmail.com",
            ),
            Member(
                name = "장지수",
                loginId = "jang1234",
                password = "!8fZc92$@9bamcf",
                email = "devhyo@gmail.com",
            ),
        )

        memberJpaRepository.save(followerMember)
        memberJpaRepository.saveAll(followingMembers)

        val memberFollows: List<MemberFollow> = followingMembers.map {
            it.incrementFollowCount()
            followerMember.incrementFollowingCount()
            MemberFollow(followingId = it.id!!, followerId = followerMember.id!!)
        }

        memberFollowJpaRepository.saveAll(memberFollows)
        entityManager.flush()
        entityManager.clear()

        // when
        val memberFollowDtos: List<MemberFollowDto> =
            memberFollowJpaRepositorySupport.findAll(followerId = followerMember.id!!, followCount = 0L)

        // then
        assertThat(memberFollowDtos).isNotEmpty
        assertThat(memberFollowDtos.size).isEqualTo(followingMembers.size)
    }
}
