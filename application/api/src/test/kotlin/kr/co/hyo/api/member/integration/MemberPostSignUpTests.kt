package kr.co.hyo.api.member.integration

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kr.co.hyo.ApiApplication
import kr.co.hyo.api.member.request.MemberSignUpRequest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(classes = [ApiApplication::class])
@DirtiesContext
@AutoConfigureMockMvc
@DisplayName("POST /members/sign-up 테스트")
class MemberPostSignUpTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `회원 가입을 하고, 상태코드 201을 응답한다`() {
        // given
        val memberSignUpRequest = MemberSignUpRequest(
            name = "장효석",
            loginId = "devhyo",
            password = "!@g90fnmvbd##sxd",
            email = "devhyo@gmail.com",
        )

        // when, then
        mockMvc
            .perform(
                post("/members/sign-up")
                    .contentType(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(memberSignUpRequest))
            )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.name").value("장효석"))
            .andExpect(jsonPath("$.loginId").value("devhyo"))
            .andExpect(jsonPath("$.email").value("devhyo@gmail.com"))
    }

    @Test
    fun `이름이 유효하지 않은 경우, 상태 코드 400을 응답한다`() {
        // given
        val memberSignUpRequest = MemberSignUpRequest(
            name = "",
            loginId = "devhyo",
            password = "!@g90fnmvbd##sxd",
            email = "devhyo@gmail.com",
        )

        // when, then
        mockMvc
            .perform(
                post("/members/sign-up")
                    .contentType(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(memberSignUpRequest))
            )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `로그인 아이디가 유효하지 않은 경우, 상태 코드 400을 응답한다`() {
        // given
        val memberSignUpRequest = MemberSignUpRequest(
            name = "장효석",
            loginId = "",
            password = "!@g90fnmvbd##sxd",
            email = "devhyo@gmail.com",
        )

        // when, then
        mockMvc
            .perform(
                post("/members/sign-up")
                    .contentType(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(memberSignUpRequest))
            )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `비밀번호가 비어 있는 경우, 상태 코드 400을 응답한다`() {
        // given
        val memberSignUpRequest = MemberSignUpRequest(
            name = "장효석",
            loginId = "devhyo",
            password = "",
            email = "devhyo@gmail.com",
        )

        // when, then
        mockMvc
            .perform(
                post("/members/sign-up")
                    .contentType(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(memberSignUpRequest))
            )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `비밀번호가 유효성 검증에 실패하는 경우, 상태 코드 400을 응답한다`() {
        // given
        val memberSignUpRequest = MemberSignUpRequest(
            name = "장효석",
            loginId = "devhyo",
            password = "1234",
            email = "devhyo@gmail.com",
        )

        // when, then
        mockMvc
            .perform(
                post("/members/sign-up")
                    .contentType(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(memberSignUpRequest))
            )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `이메일이 유효하지 않은 경우, 상태 코드 400을 응답한다`() {
        // given
        val memberSignUpRequest = MemberSignUpRequest(
            name = "장효석",
            loginId = "devhyo",
            password = "!@g90fnmvbd##sxd",
            email = "",
        )

        // when, then
        mockMvc
            .perform(
                post("/members/sign-up")
                    .contentType(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(memberSignUpRequest))
            )
            .andExpect(status().isBadRequest)
    }
}
