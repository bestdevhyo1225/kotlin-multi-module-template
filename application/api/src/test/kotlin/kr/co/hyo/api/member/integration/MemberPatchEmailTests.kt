package kr.co.hyo.api.member.integration

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kr.co.hyo.ApiApplication
import kr.co.hyo.api.member.request.MemberChangeEmailRequest
import kr.co.hyo.api.member.request.MemberSignInRequest
import kr.co.hyo.api.member.request.MemberSignUpRequest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(classes = [ApiApplication::class])
@DirtiesContext
@AutoConfigureMockMvc
@DisplayName("PATCH /api/members/email 테스트")
class MemberPatchEmailTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `회원의 이메일을 변경하고, 상태코드 200을 응답한다`() {
        // given
        val memberSignUpRequest = MemberSignUpRequest(
            name = "장효석",
            loginId = "devhyo",
            password = "!@g90fnmvbd##sxd",
            email = "devhyo@gmail.com",
        )

        mockMvc
            .perform(
                post("/api/members/sign-up")
                    .contentType(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(memberSignUpRequest))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("장효석"))
            .andExpect(jsonPath("$.loginId").value("devhyo"))
            .andExpect(jsonPath("$.email").value("devhyo@gmail.com"))

        val memberSignInRequest =
            MemberSignInRequest(loginId = memberSignUpRequest.loginId, password = memberSignUpRequest.password)

        val mvcResult: MvcResult = mockMvc
            .perform(
                post("/api/members/sign-in")
                    .contentType(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(memberSignInRequest))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").exists())
            .andReturn()

        val responseData: Map<*, *> =
            jacksonObjectMapper().readValue(mvcResult.response.contentAsString, Map::class.java)!!

        val memberChangeEmailRequest = MemberChangeEmailRequest(email = "devhyo7@gmail.com")

        // when, then
        mockMvc
            .perform(
                patch("/api/members/email")
                    .header(AUTHORIZATION, "Bearer ${responseData["accessToken"]}")
                    .contentType(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(memberChangeEmailRequest))
            )
            .andExpect(status().isOk)
    }

    @Test
    fun `회원이 존재하지 않으면, 상태코드 404을 응답한다`() {
        // given
        val memberChangeEmailRequest = MemberChangeEmailRequest(email = "devhyo7@gmail.com")

        // when, then
        mockMvc
            .perform(
                patch("/api/members/email")
                    .contentType(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(memberChangeEmailRequest))
            )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `이메일이 유효하지 않으면, 상태코드 400을 응답한다`() {
        // given
        val memberChangeEmailRequest = MemberChangeEmailRequest(email = "")

        // when, then
        mockMvc
            .perform(
                patch("/api/members/email")
                    .contentType(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(memberChangeEmailRequest))
            )
            .andExpect(status().isBadRequest)
    }
}
