package kr.co.hyo.api.member.integration

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kr.co.hyo.ApiApplication
import kr.co.hyo.api.member.controller.request.MemberFollowRequest
import kr.co.hyo.api.member.controller.request.MemberSignUpRequest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@DirtiesContext
@AutoConfigureMockMvc
@ContextConfiguration(classes = [ApiApplication::class])
@DisplayName("POST /members/follow 테스트")
class MemberPostFollowTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    @WithMockUser(username = "1")
    fun `회원 팔로우를 처리하고, 상태코드 200을 응답한다`() {
        // given
        val memberSignUpRequest1 = MemberSignUpRequest(
            name = "장효석",
            loginId = "devhyo1",
            password = "!@g90fnmvbd##sxd",
            email = "devhyo@gmail.com",
        )

        mockMvc
            .perform(
                post("/members/sign-up")
                    .contentType(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(memberSignUpRequest1))
            )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.name").value("장효석"))
            .andExpect(jsonPath("$.loginId").value("devhyo1"))
            .andExpect(jsonPath("$.email").value("devhyo@gmail.com"))

        val memberSignUpRequest2 = MemberSignUpRequest(
            name = "장효석",
            loginId = "devhyo2",
            password = "!@g90fnmvbd##sxd",
            email = "devhyo@gmail.com",
        )

        mockMvc
            .perform(
                post("/members/sign-up")
                    .contentType(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(memberSignUpRequest2))
            )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.name").value("장효석"))
            .andExpect(jsonPath("$.loginId").value("devhyo2"))
            .andExpect(jsonPath("$.email").value("devhyo@gmail.com"))

        val memberFollowRequest = MemberFollowRequest(memberId = 2L)

        // when, then
        mockMvc
            .perform(
                post("/members/follow")
                    .contentType(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(memberFollowRequest))
            )
            .andExpect(status().isOk)
    }

    @Test
    fun `AccessToken이 없으면, 상태코드 401을 응답한다`() {
        // given
        val memberFollowRequest = MemberFollowRequest(memberId = 2L)

        // when, then
        mockMvc
            .perform(
                post("/members/follow")
                    .contentType(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(memberFollowRequest))
            )
            .andExpect(status().isUnauthorized)
    }
}
