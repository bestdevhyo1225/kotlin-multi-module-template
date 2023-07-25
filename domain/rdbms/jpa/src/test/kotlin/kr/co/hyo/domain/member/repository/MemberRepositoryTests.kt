package kr.co.hyo.domain.member.repository

import jakarta.persistence.EntityManager
import kr.co.hyo.config.JpaConfig
import kr.co.hyo.domain.member.entity.Member
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ContextConfiguration

@DataJpaTest
@ContextConfiguration(classes = [JpaConfig::class])
@DisplayName("MemberRepository 단위 테스트")
class MemberRepositoryTests {

    @Autowired
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var memberRepository: MemberRepository

    @Test
    fun `회원을 저장한다`() {
        // given
        val member = Member(
            name = "장효석",
            loginId = "hyo1225",
            password = "!8fZc92$@9bamcf",
            email = "devhyo@gmail.com",
        )

        // when
        memberRepository.save(member)
        entityManager.flush()
        entityManager.clear()

        // then
        val findMember: Member? = memberRepository.findByIdOrNull(member.id)

        assertThat(findMember).isNotNull
        assertThat(findMember).isEqualTo(member)
    }

    @Test
    fun `회원의 이메일을 수정한다`() {
        // given
        val member = Member(
            name = "장효석",
            loginId = "hyo1225",
            password = "!8fZc92$@9bamcf",
            email = "devhyo@gmail.com",
        )

        memberRepository.save(member)
        entityManager.flush()
        entityManager.clear()

        // when
        val findMember: Member = memberRepository.findByIdOrNull(member.id) ?: throw NoSuchElementException()

        findMember.changeEmail(email = "devhyo7@gmail.com")
        entityManager.flush()
        entityManager.clear()

        // then
        assertThat(findMember.email).isEqualTo("devhyo7@gmail.com")
        assertThat(findMember.updatedDatetime).isNotEqualTo(member.updatedDatetime)
    }

    @Test
    fun `회원의 이메일이 변경되지 않았으면, 수정하지 않는다`() {
        // given
        val member = Member(
            name = "장효석",
            loginId = "hyo1225",
            password = "!8fZc92$@9bamcf",
            email = "devhyo@gmail.com",
        )

        memberRepository.save(member)
        entityManager.flush()
        entityManager.clear()

        // when
        val findMember: Member = memberRepository.findByIdOrNull(member.id) ?: throw NoSuchElementException()

        findMember.changeEmail(email = member.email)
        entityManager.flush()
        entityManager.clear()

        // then
        assertThat(findMember.email).isEqualTo(member.email)
        assertThat(findMember.updatedDatetime).isEqualTo(member.updatedDatetime)
    }
}
