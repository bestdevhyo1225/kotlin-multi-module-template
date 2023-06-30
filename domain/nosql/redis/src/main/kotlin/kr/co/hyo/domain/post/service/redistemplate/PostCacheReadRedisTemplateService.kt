package kr.co.hyo.domain.post.service.redistemplate

import kr.co.hyo.domain.post.dto.PostCacheDto
import kr.co.hyo.domain.post.entity.PostCache
import kr.co.hyo.domain.post.repository.PostRedisTemplateRepository
import kr.co.hyo.domain.post.service.PostCacheReadService
import org.springframework.stereotype.Service

@Service
class PostCacheReadRedisTemplateService(
    private val postRedisTemplateRepository: PostRedisTemplateRepository,
) : PostCacheReadService {

    override fun findPostCache(postId: Long): PostCacheDto? {
        val postCache = PostCache(postId = postId)
        val key: String = postCache.getPostKey()
        return postRedisTemplateRepository.get(key = key, clazz = PostCacheDto::class.java)
    }

    override fun findPostCacheLikeCount(postId: Long): Long? {
        val postCache = PostCache(postId = postId)
        val key: String = postCache.getPostLikeCountKey()
        return postRedisTemplateRepository.get(key = key, clazz = Long::class.java)
    }

    override fun findPostCacheViewCount(postId: Long): Long? {
        val postCache = PostCache(postId = postId)
        val key: String = postCache.getPostViewCountKey()
        return postRedisTemplateRepository.get(key = key, clazz = Long::class.java)
    }
}
