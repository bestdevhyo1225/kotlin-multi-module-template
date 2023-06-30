package kr.co.hyo.api.post.service

import kr.co.hyo.domain.post.dto.PostCacheCreateDto
import kr.co.hyo.domain.post.dto.PostCacheDto
import kr.co.hyo.domain.post.dto.PostDto
import kr.co.hyo.domain.post.service.PostCacheReadService
import kr.co.hyo.domain.post.service.PostCacheWriteService
import kr.co.hyo.domain.post.service.PostReadService
import org.springframework.stereotype.Service

@Service
class PostDetailService(
    private val postCacheReadService: PostCacheReadService,
    private val postCacheWriteService: PostCacheWriteService,
    private val postReadService: PostReadService,
) {

    fun findPost(postId: Long): PostDto {
        val postCacheDto: PostCacheDto? = postCacheReadService.findPostCache(postId = postId)
        if (postCacheDto == null) {
            val postDto: PostDto = postReadService.findPost(postId = postId)
            postCacheWriteService.create(dto = toPostCacheCreateDto(postDto = postDto))
            return postDto
        }
        val postCacheLikeCount: Long? = postCacheReadService.findPostCacheLikeCount(postId = postId)
        val postCacheViewCount: Long? = postCacheReadService.findPostCacheViewCount(postId = postId)
        if (postCacheLikeCount == null || postCacheViewCount == null) {
            val postLikeCount: Long = postReadService.findPostLikeCount(postId = postId)
            val postViewCount: Long = postReadService.findPostViewCount(postId = postId)
            postCacheWriteService.createLikeCount(postId = postId, postLikeCount = postLikeCount)
            postCacheWriteService.createViewCount(postId = postId, postViewCount = postViewCount)
            return toPostDto(
                postCacheDto = postCacheDto,
                postLikeCount = postLikeCount,
                postViewCount = postViewCount,
            )
        }
        return toPostDto(
            postCacheDto = postCacheDto,
            postLikeCount = postCacheLikeCount,
            postViewCount = postCacheViewCount,
        )
    }

    private fun toPostCacheCreateDto(postDto: PostDto): PostCacheCreateDto {
        return with(receiver = postDto) {
            PostCacheCreateDto(
                postId = id,
                memberId = memberId,
                title = title,
                contents = contents,
                likeCount = likeCount,
                viewCount = viewCount,
                createdDate = createdDate,
                createdDatetime = createdDatetime,
            )
        }
    }

    private fun toPostDto(postCacheDto: PostCacheDto, postLikeCount: Long, postViewCount: Long): PostDto {
        return with(receiver = postCacheDto) {
            PostDto(
                id = postId,
                memberId = memberId,
                title = title,
                contents = contents,
                likeCount = postLikeCount,
                viewCount = postViewCount,
                createdDate = createdDate,
                createdDatetime = createdDatetime,
            )
        }
    }
}
