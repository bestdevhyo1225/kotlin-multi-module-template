package kr.co.hyo.domain.member.entity

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Member 단위 테스트")
class MemberTests {

    @Test
    fun `회원을 생성한다`() {
        // given
        val name = "장효석"
        val loginId = "hyo1225"
        val password = "!8fZc92$@9bamcf"
        val email = "devhyo@gmail.com"

        // when
        val member = Member(
            name = name,
            loginId = loginId,
            password = password,
            email = email,
        )

        // then
        assertThat(member.name).isEqualTo("장효석")
        assertThat(member.loginId).isEqualTo("hyo1225")
        assertThatCode { member.verifyPassword(password = password) }.doesNotThrowAnyException()
        assertThat(member.email).isEqualTo("devhyo@gmail.com")
    }

    @Test
    fun `회원의 비밀번호를 변경한다`() {
        // given
        val password = "!8fZc92$@9bamcf"
        val member = Member(
            name = "장효석",
            loginId = "hyo1225",
            password = password,
            email = "devhyo@gmail.com",
        )
        val newPassword = "!2398u1@#Bvzjdqsdq"

        // when
        member.changePassword(oldPassword = password, newPassword = newPassword)

        // then
        assertThatCode { member.verifyPassword(password = newPassword) }.doesNotThrowAnyException()
    }

    @Test
    fun `회원의 이메일을 변경한다`() {
        // given
        val member = Member(
            name = "장효석",
            loginId = "hyo1225",
            password = "!8fZc92$@9bamcf",
            email = "devhyo@gmail.com",
        )

        // when
        member.changeEmail(email = "devhyo7@gmail.com")

        // then
        assertThat(member.email).isEqualTo("devhyo7@gmail.com")
    }
}
