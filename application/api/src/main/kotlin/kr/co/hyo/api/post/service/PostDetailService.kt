package kr.co.hyo.api.post.service

import kr.co.hyo.api.post.mapper.PostDomainDtoMapper
import kr.co.hyo.domain.post.dto.PostCacheDto
import kr.co.hyo.domain.post.dto.PostDto
import kr.co.hyo.domain.post.service.PostCacheReadService
import kr.co.hyo.domain.post.service.PostCacheWriteService
import kr.co.hyo.domain.post.service.PostLikeReadService
import kr.co.hyo.domain.post.service.PostReadService
import kr.co.hyo.domain.post.service.PostViewWriteService
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class PostDetailService(
    private val postCacheReadService: PostCacheReadService,
    private val postCacheWriteService: PostCacheWriteService,
    private val postLikeReadService: PostLikeReadService,
    private val postViewWriteService: PostViewWriteService,
    private val postReadService: PostReadService,
) {

    private val logger = KotlinLogging.logger {}

    fun findPost(postId: Long, memberId: Long, tokenMemberId: Long): PostDto {
        if (memberId == tokenMemberId) {
            logger.info { "Member's own posts (postId: $postId, memberId: $memberId)" }
            return postReadService.findPostFromPrimaryDB(postId = postId, memberId = memberId)
        }

        val postCacheDto: PostCacheDto = postCacheReadService.findPostCache(postId = postId, memberId = memberId)
            ?: let {
                val postDto: PostDto = postReadService.findPost(postId = postId, memberId = memberId)
                postCacheWriteService.createPostCache(dto = PostDomainDtoMapper.toPostCacheCreateDto(postDto = postDto))
            }
        val postLikeCount: Long = postLikeReadService.count(postId = postId)
        val postViewCount: Long = postViewWriteService.increment(
            postId = postId,
            postOwnMemberId = postCacheDto.memberId,
            memberId = memberId,
        )

        return PostDomainDtoMapper.toPostDto(
            postCacheDto = postCacheDto,
            postLikeCount = postLikeCount,
            postViewCount = postViewCount,
        )
    }
}
