package kr.co.hyo.domain.post.service.impl

import kr.co.hyo.domain.post.dto.PostCacheDto
import kr.co.hyo.domain.post.entity.PostCache
import kr.co.hyo.domain.post.repository.PostRedisTemplateRepository
import kr.co.hyo.domain.post.service.PostCacheReadService
import org.springframework.stereotype.Service

@Service
class PostCacheReadServiceImpl(
    private val postRedisTemplateRepository: PostRedisTemplateRepository,
) : PostCacheReadService {

    override fun isPostRecentlyUpdate(postId: Long): Boolean {
        val key: String = PostCache.getPostUpdateKey(postId = postId)
        return postRedisTemplateRepository.get(key = key, clazz = Boolean::class.java) != null
    }

    override fun findPostCache(postId: Long, memberId: Long): PostCacheDto? {
        val postCache = PostCache(postId = postId, memberId = memberId)
        val key: String = postCache.getPostKey()
        return postRedisTemplateRepository.get(key = key, clazz = PostCacheDto::class.java)
    }
}
