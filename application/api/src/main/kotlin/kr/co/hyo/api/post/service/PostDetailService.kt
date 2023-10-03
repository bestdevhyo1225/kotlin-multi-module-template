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
        // 본인 게시물인 경우, Primary DB -> Replica DB 복제 지연을 대비해서 최근 수정 여부를 확인하고,
        var isPostRecentlyUpdate = false
        if (memberId == tokenMemberId) {
            isPostRecentlyUpdate = postCacheReadService.isPostRecentlyUpdate(postId = postId)
        }
        // 최근에 수정했으면, Primary DB를 통해 게시물을 조회한다.
        if (isPostRecentlyUpdate) {
            logger.info { "Member's own posts (postId: $postId, memberId: $memberId)" }
            return postReadService.findPostFromPrimaryDB(postId = postId, memberId = memberId)
        }

        // 그 외의 다른 사용자가 조회하는 경우에는 Redis 또는 Replica DB를 통해 게시물을 조회한다.
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
