package kr.co.hyo.api.post.service

import kr.co.hyo.domain.post.dto.PostCacheCreateDto
import kr.co.hyo.domain.post.dto.PostCacheDto
import kr.co.hyo.domain.post.dto.PostDto
import kr.co.hyo.domain.post.service.PostCacheReadService
import kr.co.hyo.domain.post.service.PostCacheWriteService
import kr.co.hyo.domain.post.service.PostLikeReadService
import kr.co.hyo.domain.post.service.PostReadService
import kr.co.hyo.domain.post.service.PostViewWriteService
import org.springframework.stereotype.Service

@Service
class PostDetailService(
    private val postCacheReadService: PostCacheReadService,
    private val postCacheWriteService: PostCacheWriteService,
    private val postLikeReadService: PostLikeReadService,
    private val postViewWriteService: PostViewWriteService,
    private val postReadService: PostReadService,
) {

    fun findPost(postId: Long, memberId: Long): PostDto {
        val postCacheDto: PostCacheDto = postCacheReadService.findPostCache(postId = postId)
            ?: let {
                val postDto: PostDto = postReadService.findPost(postId = postId)
                postCacheWriteService.create(dto = toPostCacheCreateDto(postDto = postDto))
            }
        val postLikeCount: Long = postLikeReadService.count(postId = postId)
        val postViewCount: Long = postViewWriteService.increment(
            postId = postId,
            postOwnMemberId = postCacheDto.memberId,
            memberId = memberId,
        )
        return toPostDto(
            postCacheDto = postCacheDto,
            postLikeCount = postLikeCount,
            postViewCount = postViewCount,
        )
    }

    private fun toPostCacheCreateDto(postDto: PostDto): PostCacheCreateDto {
        return with(receiver = postDto) {
            PostCacheCreateDto(
                postId = id,
                memberId = memberId,
                title = title,
                contents = contents,
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
