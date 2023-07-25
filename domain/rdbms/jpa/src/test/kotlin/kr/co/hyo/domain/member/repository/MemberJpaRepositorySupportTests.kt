package kr.co.hyo.domain.member.repository

import jakarta.persistence.EntityManager
import kr.co.hyo.config.JpaConfig
import kr.co.hyo.domain.member.entity.Member
import kr.co.hyo.domain.member.repository.querydsl.MemberJpaQueryDslRepositorySupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration

@DataJpaTest
@ContextConfiguration(classes = [JpaConfig::class, MemberJpaQueryDslRepositorySupport::class])
@DisplayName("MemberJpaRepositorySupport 단위 테스트")
class MemberJpaRepositorySupportTests {

    @Autowired
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var memberJpaRepository: MemberJpaRepository

    @Autowired
    lateinit var memberJpaRepositorySupport: MemberJpaRepositorySupport

    @Test
    fun `로그인 아아디로 회원을 조회한다`() {
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

        // when
        val findMember: Member? = memberJpaRepositorySupport.find(loginId = member.loginId)

        // then
        assertThat(findMember).isNotNull
        assertThat(findMember).isEqualTo(member)
    }
}
