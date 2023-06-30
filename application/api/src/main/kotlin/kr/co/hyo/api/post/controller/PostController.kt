package kr.co.hyo.api.post.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kr.co.hyo.api.post.controller.request.PostCreateRequest
import kr.co.hyo.api.post.service.PostDetailService
import kr.co.hyo.api.post.service.PostFanoutService
import kr.co.hyo.api.post.service.PostTimelineService
import kr.co.hyo.common.util.page.PageByPosition
import kr.co.hyo.common.util.page.PageRequestByPosition
import kr.co.hyo.domain.post.dto.PostDto
import kr.co.hyo.domain.post.service.PostLikeWriteService
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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
    private val postDetailService: PostDetailService,
    private val postFanoutService: PostFanoutService,
    private val postLikeWriteService: PostLikeWriteService,
    private val postTimelineService: PostTimelineService,
) {

    @PostMapping
    @ResponseStatus(value = CREATED)
    @Operation(description = "게시글 등록")
    fun posts(
        authentication: Authentication,
        @Valid @RequestBody request: PostCreateRequest,
    ): ResponseEntity<PostDto> {
        val memberId: Long = authentication.name.toLong()
        val dto: PostDto = postFanoutService.createPost(dto = request.toDto(memberId = memberId))
        return ResponseEntity
            .created(URI.create("/posts/" + dto.id))
            .body(dto)
    }

    @PostMapping("/{id}/like")
    @Operation(description = "게시글 좋아요")
    fun postsLike(
        authentication: Authentication,
        @PathVariable
        @Parameter(schema = Schema(description = "게시글 번호", example = "1"))
        id: Long,
    ) {
        val memberId: Long = authentication.name.toLong()
        postLikeWriteService.likePost(postId = id, memberId = memberId)
    }

    @DeleteMapping("/{id}/like")
    @Operation(description = "게시글 좋아요 취소")
    fun deletePostsLike(
        authentication: Authentication,
        @PathVariable
        @Parameter(schema = Schema(description = "게시글 번호", example = "1"))
        id: Long,
    ) {
        val memberId: Long = authentication.name.toLong()
        postLikeWriteService.deleteLikePost(postId = id, memberId = memberId)
    }

    @GetMapping("/timeline/refresh")
    @Operation(description = "게시글 타임라인 갱신")
    fun postsMembersTimelineRefresh(authentication: Authentication) {
        val memberId: Long = authentication.name.toLong()
        postTimelineService.refreshPosts(memberId = memberId)
    }

    @GetMapping("/timeline")
    @Operation(description = "게시글 타임라인 조회")
    fun postsMembersTimeline(
        authentication: Authentication,
        @Valid pageRequestByPosition: PageRequestByPosition,
    ): ResponseEntity<PageByPosition<PostDto>> {
        val memberId: Long = authentication.name.toLong()
        val pagePostDto: PageByPosition<PostDto> =
            postTimelineService.findPosts(memberId = memberId, pageRequestByPosition = pageRequestByPosition)
        return ResponseEntity.ok(pagePostDto)
    }

    @GetMapping("/{id}")
    @Operation(description = "게시글 상세 조회")
    fun postsId(
        authentication: Authentication,
        @PathVariable
        @Parameter(schema = Schema(description = "게시글 번호", example = "1"))
        id: Long,
    ): ResponseEntity<PostDto> {
        val memberId: Long = authentication.name.toLong()
        val postDto: PostDto = postDetailService.findPost(postId = id, memberId = memberId)
        return ResponseEntity.ok(postDto)
    }
}
