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

    override fun findPostCache(postId: Long): PostCacheDto? {
        val postCache = PostCache(postId = postId)
        val key: String = postCache.getPostKey()
        return postRedisTemplateRepository.get(key = key, clazz = PostCacheDto::class.java)
    }
}
