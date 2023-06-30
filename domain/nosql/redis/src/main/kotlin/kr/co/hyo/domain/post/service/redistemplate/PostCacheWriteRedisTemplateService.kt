package kr.co.hyo.domain.post.service.redistemplate

import kr.co.hyo.domain.post.dto.PostCacheCreateDto
import kr.co.hyo.domain.post.dto.PostCacheDto
import kr.co.hyo.domain.post.entity.PostCache
import kr.co.hyo.domain.post.repository.PostRedisTemplateRepository
import kr.co.hyo.domain.post.service.PostCacheWriteService
import org.springframework.stereotype.Service

@Service
class PostCacheWriteRedisTemplateService(
    private val postRedisTemplateRepository: PostRedisTemplateRepository,
) : PostCacheWriteService {

    override fun create(dto: PostCacheCreateDto) {
        val postCache = PostCache(postId = dto.postId)
        val (postKey: String, expirationTimeMs: Long) = postCache.getPostKeyAndExpirationTimeMs()
        val value: PostCacheDto = dto.toPostCacheDto()
        val postLikeCountKey: String = postCache.getPostLikeCountKey()
        val postViewCountKey: String = postCache.getPostViewCountKey()
        postRedisTemplateRepository.set(key = postKey, value = value, expirationTimeMs = expirationTimeMs)
        postRedisTemplateRepository.set(
            key = postLikeCountKey,
            value = dto.likeCount,
            expirationTimeMs = expirationTimeMs,
        )
        postRedisTemplateRepository.set(
            key = postViewCountKey,
            value = dto.viewCount,
            expirationTimeMs = expirationTimeMs,
        )
    }

    override fun createLikeCount(postId: Long, postLikeCount: Long) {
        val postCache = PostCache(postId = postId)
        val key: String = postCache.getPostLikeCountKey()
        val expirationTimeMs: Long = postCache.getExpirationTimeMs()
        postRedisTemplateRepository.set(key = key, value = postLikeCount, expirationTimeMs = expirationTimeMs)
    }

    override fun createViewCount(postId: Long, postViewCount: Long) {
        val postCache = PostCache(postId = postId)
        val key: String = postCache.getPostViewCountKey()
        val expirationTimeMs: Long = postCache.getExpirationTimeMs()
        postRedisTemplateRepository.set(key = key, value = postViewCount, expirationTimeMs = expirationTimeMs)
    }
}
