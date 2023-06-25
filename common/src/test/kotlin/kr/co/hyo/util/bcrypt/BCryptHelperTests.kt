package kr.co.hyo.util.bcrypt

import kr.co.hyo.common.util.bcrpyt.BCryptHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("BCryptHelper 단위 테스트")
class BCryptHelperTests {

    @Test
    fun `비밀번호를 암호화한다`() {
        // given
        val password = "!c9#9@#csZdx1^9d"

        // when
        val encryptedPassword = BCryptHelper.encrypt(password = password)

        // then
        assertThat(BCryptHelper.isMatch(password, encryptedPassword)).isTrue()
        assertThat(BCryptHelper.isNotMatch(password, encryptedPassword)).isFalse()
    }

    @Test
    fun `비밀번호가 다른 경우 false 값을 반환한다`() {
        // given
        val encryptedPassword = BCryptHelper.encrypt(password = "!c9#9@#csZdx1^9d")

        // when
        val actual: Boolean = BCryptHelper.isNotMatch("1234", encryptedPassword)

        // then
        assertThat(actual).isTrue()
    }
}
