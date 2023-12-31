package kr.co.hyo.api.member.integration

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kr.co.hyo.ApiApplication
import kr.co.hyo.api.member.request.MemberChangePasswordRequest
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
@DisplayName("PATCH /api/members/password 테스트")
class MemberPatchPasswordTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `회원의 비밀번호를 변경하고, 상태코드 200을 응답한다`() {
        // given
        val memberSignUpRequest = MemberSignUpRequest(
            name = "장효석",
            loginId = "devhyo1",
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
            .andExpect(jsonPath("$.loginId").value("devhyo1"))
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

        val memberChangePasswordRequest = MemberChangePasswordRequest(
            oldPassword = memberSignUpRequest.password,
            newPassword = "913nlkv@#kdfcs9",
        )

        // when, then
        mockMvc
            .perform(
                patch("/api/members/password")
                    .header(AUTHORIZATION, "Bearer ${responseData["accessToken"]}")
                    .contentType(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(memberChangePasswordRequest))
            )
            .andExpect(status().isOk)
    }

    @Test
    fun `기존 비밀번호가 비어 있는 경우, 상태코드 400을 응답한다`() {
        // given
        val memberSignUpRequest = MemberSignUpRequest(
            name = "장효석",
            loginId = "devhyo2",
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
            .andExpect(jsonPath("$.loginId").value("devhyo2"))
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

        val memberChangePasswordRequest = MemberChangePasswordRequest(
            oldPassword = "",
            newPassword = "913nlkv@#kdfcs9",
        )

        // when, then
        mockMvc
            .perform(
                patch("/api/members/password")
                    .header(AUTHORIZATION, "Bearer ${responseData["accessToken"]}")
                    .contentType(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(memberChangePasswordRequest))
            )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `변경할 비밀번호가 비어 있는 경우, 상태코드 400을 응답한다`() {
        // given
        val memberSignUpRequest = MemberSignUpRequest(
            name = "장효석",
            loginId = "devhyo3",
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
            .andExpect(jsonPath("$.loginId").value("devhyo3"))
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

        val memberChangePasswordRequest = MemberChangePasswordRequest(
            oldPassword = "1@3,9cv$%zxc26dz",
            newPassword = "",
        )

        // when, then
        mockMvc
            .perform(
                patch("/api/members/password")
                    .header(AUTHORIZATION, "Bearer ${responseData["accessToken"]}")
                    .contentType(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(memberChangePasswordRequest))
            )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `기존 비밀번호가 유효성 검증에 실패하는 경우, 상태 코드 400을 응답한다`() {
        // given
        val memberSignUpRequest = MemberSignUpRequest(
            name = "장효석",
            loginId = "devhyo4",
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
            .andExpect(jsonPath("$.loginId").value("devhyo4"))
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

        val memberChangePasswordRequest = MemberChangePasswordRequest(
            oldPassword = "1234",
            newPassword = "913nlkv@#kdfcs9",
        )

        // when, then
        mockMvc
            .perform(
                patch("/api/members/password")
                    .header(AUTHORIZATION, "Bearer ${responseData["accessToken"]}")
                    .contentType(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(memberChangePasswordRequest))
            )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `변경할 비밀번호가 유효성 검증에 실패하는 경우, 상태 코드 400을 응답한다`() {
        // given
        val memberSignUpRequest = MemberSignUpRequest(
            name = "장효석",
            loginId = "devhyo5",
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
            .andExpect(jsonPath("$.loginId").value("devhyo5"))
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

        val memberChangePasswordRequest = MemberChangePasswordRequest(
            oldPassword = "1@3,9cv$%zxc26dz",
            newPassword = "1234",
        )

        // when, then
        mockMvc
            .perform(
                patch("/api/members/password")
                    .header(AUTHORIZATION, "Bearer ${responseData["accessToken"]}")
                    .contentType(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(memberChangePasswordRequest))
            )
            .andExpect(status().isBadRequest)
    }
}
