package kr.co.hyo.api.post.integration

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kr.co.hyo.ApiApplication
import kr.co.hyo.api.member.request.MemberSignInRequest
import kr.co.hyo.api.member.request.MemberSignUpRequest
import kr.co.hyo.api.post.request.PostCreateRequest
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(classes = [ApiApplication::class])
@DirtiesContext
@AutoConfigureMockMvc
@DisplayName("POST /api/posts 테스트")
class PostPostCreateTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `게시글을 생성하고, 상태코드 201을 응답한다`() {
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
            .andExpect(status().isCreated)
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

        val postCreateRequest = PostCreateRequest(title = "제목!", contents = "내용!")

        // when, then
        mockMvc
            .perform(
                post("/api/posts")
                    .header(AUTHORIZATION, "Bearer ${responseData["accessToken"]}")
                    .contentType(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(postCreateRequest))
            )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.memberId").exists())
            .andExpect(jsonPath("$.title").value("제목!"))
            .andExpect(jsonPath("$.contents").value("내용!"))
            .andExpect(jsonPath("$.likeCount").value(0))
            .andExpect(jsonPath("$.viewCount").value(0))
    }
}
