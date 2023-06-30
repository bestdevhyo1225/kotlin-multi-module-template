package kr.co.hyo.api.post.service

import kr.co.hyo.domain.post.dto.PostCacheCreateDto
import kr.co.hyo.domain.post.dto.PostCacheDto
import kr.co.hyo.domain.post.dto.PostDto
import kr.co.hyo.domain.post.service.PostCacheReadService
import kr.co.hyo.domain.post.service.PostCacheWriteService
import kr.co.hyo.domain.post.service.PostLikeReadService
import kr.co.hyo.domain.post.service.PostReadService
import org.springframework.stereotype.Service

@Service
class PostDetailService(
    private val postCacheReadService: PostCacheReadService,
    private val postCacheWriteService: PostCacheWriteService,
    private val postLikeReadService: PostLikeReadService,
    private val postReadService: PostReadService,
) {

    fun findPost(postId: Long, memberId: Long): PostDto {
        var postCacheDto: PostCacheDto? = postCacheReadService.findPostCache(postId = postId)
        val postLikeCount: Long = postLikeReadService.count(postId = postId)
        if (postCacheDto == null) {
            val postDto: PostDto = postReadService.findPost(postId = postId)
            postCacheDto = postCacheWriteService.create(dto = toPostCacheCreateDto(postDto = postDto))
        }
        return toPostDto(
            postCacheDto = postCacheDto,
            postLikeCount = postLikeCount,
            postViewCount = 0,
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
