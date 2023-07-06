package kr.co.hyo.api.member.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import kr.co.hyo.common.util.validator.Password
import kr.co.hyo.domain.member.dto.MemberCreateDto

data class MemberSignUpRequest(
    @field:NotBlank(message = "이름을 입력하세요")
    @field:Schema(description = "이름", example = "장효석", required = true)
    val name: String,

    @field:NotBlank(message = "로그인 아이디를 입력하세요")
    @field:Schema(description = "로그인 아이디", example = "devhyo1", required = true)
    val loginId: String,

    @field:NotBlank(message = "비밀번호를 입력하세요")
    @field:Password
    @field:Schema(description = "비밀번호", example = "!@zdx90b8Cl3x2%&dz", required = true)
    val password: String,

    @field:NotBlank(message = "이메일을 입력하세요")
    @field:Schema(description = "이메일", example = "devhyo@gmail.com", required = true)
    val email: String,
) {

    fun toDto(): MemberCreateDto =
        MemberCreateDto(
            name = name,
            loginId = loginId,
            password = password,
            email = email,
        )
}
