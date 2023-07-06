package kr.co.hyo.api.post.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import kr.co.hyo.domain.post.dto.PostCreateDto

data class PostCreateRequest(
    @field:NotBlank(message = "게시글 제목을 입력하세요")
    @field:Schema(description = "게시글 제목", example = "2023.06.29 놀러감~", required = true)
    val title: String,

    @field:NotBlank(message = "게시글 내용을 입력하세요")
    @field:Schema(description = "게시글 내용", example = "제주도가서 놀러갔다 왔음~!", required = true)
    val contents: String,
) {

    fun toDto(memberId: Long): PostCreateDto =
        PostCreateDto(
            memberId = memberId,
            title = title,
            contents = contents,
        )
}
