package kr.co.hyo.api.post.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.security.SecurityRequirements
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kr.co.hyo.api.post.request.PostCreateRequest
import kr.co.hyo.api.post.service.PostDetailService
import kr.co.hyo.api.post.service.PostFanoutService
import kr.co.hyo.api.post.service.PostSearchService
import kr.co.hyo.api.post.service.PostTimelineService
import kr.co.hyo.common.util.auth.Auth
import kr.co.hyo.common.util.auth.AuthInfo
import kr.co.hyo.common.util.limiter.RequestLimiter
import kr.co.hyo.common.util.page.PageByPosition
import kr.co.hyo.common.util.page.PageRequestByPosition
import kr.co.hyo.domain.post.dto.PostDto
import kr.co.hyo.domain.post.service.PostLikeWriteService
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
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
@RequestMapping("/api/posts")
@Tag(name = "게시글", description = "API Document")
class PostController(
    private val postDetailService: PostDetailService,
    private val postFanoutService: PostFanoutService,
    private val postLikeWriteService: PostLikeWriteService,
    private val postSearchService: PostSearchService,
    private val postTimelineService: PostTimelineService,
) {

    @PostMapping
    @ResponseStatus(value = CREATED)
    @Operation(description = "게시글 등록")
    @RequestLimiter(count = 100)
    fun postPosts(
        @Auth @Parameter(hidden = true) authInfo: AuthInfo,
        @Valid @RequestBody request: PostCreateRequest,
    ): ResponseEntity<PostDto> {
        val dto: PostDto = postFanoutService.createPost(dto = request.toDto(memberId = authInfo.memberId))
        return ResponseEntity
            .created(URI.create("/api/posts/" + dto.id))
            .body(dto)
    }

    @PostMapping("/{id}/like")
    @Operation(description = "게시글 좋아요")
    fun getPostsLike(
        @Auth @Parameter(hidden = true) authInfo: AuthInfo,
        @PathVariable @Parameter(schema = Schema(description = "게시글 번호", example = "1")) id: Long,
    ) {
        postLikeWriteService.likePost(postId = id, memberId = authInfo.memberId)
    }

    @DeleteMapping("/{id}/like")
    @Operation(description = "게시글 좋아요 취소")
    fun deletePostsLike(
        @Auth @Parameter(hidden = true) authInfo: AuthInfo,
        @PathVariable @Parameter(schema = Schema(description = "게시글 번호", example = "1")) id: Long,
    ) {
        postLikeWriteService.deleteLikePost(postId = id, memberId = authInfo.memberId)
    }

    @GetMapping("/timeline/refresh")
    @Operation(description = "게시글 타임라인 갱신")
    fun getPostsTimelineRefresh(@Auth @Parameter(hidden = true) authInfo: AuthInfo) {
        postTimelineService.refreshPosts(memberId = authInfo.memberId)
    }

    @GetMapping("/timeline")
    @Operation(description = "게시글 타임라인 조회")
    fun getPostsTimeline(
        @Auth @Parameter(hidden = true) authInfo: AuthInfo,
        @Valid pageRequestByPosition: PageRequestByPosition,
    ): ResponseEntity<PageByPosition<PostDto>> {
        val pagePostDto: PageByPosition<PostDto> =
            postTimelineService.findPosts(memberId = authInfo.memberId, pageRequestByPosition = pageRequestByPosition)
        return ResponseEntity.ok(pagePostDto)
    }

    @GetMapping("/{keyword}/search")
    @Operation(description = "게시글 검색")
    @SecurityRequirements
    fun getPostsSearch(
        @PathVariable
        @Parameter(schema = Schema(description = "게시글 검색 키워드", example = "테스트"))
        keyword: String,
        @Valid
        pageRequestByPosition: PageRequestByPosition,
    ): ResponseEntity<PageByPosition<PostDto>> {
        val pagePostDto: PageByPosition<PostDto> =
            postSearchService.search(keyword = keyword, pageRequestByPosition = pageRequestByPosition)
        return ResponseEntity.ok(pagePostDto)
    }

    @GetMapping("/{id}/members/{memberId}")
    @Operation(description = "게시글 상세 조회")
    fun postsId(
        @Auth @Parameter(hidden = true) authInfo: AuthInfo,
        @PathVariable @Parameter(schema = Schema(description = "게시글 번호", example = "1")) id: Long,
        @PathVariable @Parameter(schema = Schema(description = "회원 번호", example = "1")) memberId: Long,
    ): ResponseEntity<PostDto> {
        val postDto: PostDto =
            postDetailService.findPost(postId = id, memberId = memberId, tokenMemberId = authInfo.memberId)
        return ResponseEntity.ok(postDto)
    }
}
