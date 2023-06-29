package kr.co.hyo.api.post.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kr.co.hyo.api.post.controller.request.PostCreateRequest
import kr.co.hyo.api.post.service.PostFanoutService
import kr.co.hyo.domain.post.dto.PostDto
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/posts")
@Tag(name = "게시글", description = "API Document")
class PostController(
    private val postFanoutService: PostFanoutService,
) {

    @PostMapping
    @ResponseStatus(value = CREATED)
    @Operation(description = "게시글 등록")
    fun create(
        authentication: Authentication,
        @Valid @RequestBody request: PostCreateRequest,
    ): ResponseEntity<PostDto> {
        val memberId: Long = authentication.name.toLong()
        val dto: PostDto = postFanoutService.createPost(dto = request.toDto(memberId = memberId))
        return ResponseEntity
            .created(URI.create("/posts/" + dto.id))
            .body(dto)
    }
}
