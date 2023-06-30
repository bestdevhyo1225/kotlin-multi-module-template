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

    fun findPost(id: Long): PostDto {
        val postCacheDto: PostCacheDto? = postCacheReadService.findPostCache(postId = id)
        if (postCacheDto == null) {
            val postDto: PostDto = postReadService.findPost(postId = id)
            postCacheWriteService.create(dto = toPostCacheCreateDto(postDto = postDto))
            return postDto
        }
        val postCacheLikeCount: Long? = postCacheReadService.findPostCacheLikeCount(postId = id)
        val postCacheViewCount: Long? = postCacheReadService.findPostCacheViewCount(postId = id)
        if (postCacheLikeCount == null || postCacheViewCount == null) {
            val postLikeCount: Long = postReadService.findPostLikeCount(postId = id)
            val postViewCount: Long = postReadService.findPostViewCount(postId = id)
            postCacheWriteService.createLikeCount(postId = id, postLikeCount = postLikeCount)
            postCacheWriteService.createViewCount(postId = id, postViewCount = postViewCount)
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
