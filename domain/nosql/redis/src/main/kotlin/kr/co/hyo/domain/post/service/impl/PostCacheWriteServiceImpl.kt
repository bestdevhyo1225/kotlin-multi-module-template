package kr.co.hyo.domain.post.service.impl

import kr.co.hyo.domain.post.dto.PostCacheCreateDto
import kr.co.hyo.domain.post.dto.PostCacheDto
import kr.co.hyo.domain.post.entity.PostCache
import kr.co.hyo.domain.post.repository.PostRedisTemplateRepository
import kr.co.hyo.domain.post.service.PostCacheWriteService
import org.springframework.stereotype.Service

@Service
class PostCacheWriteServiceImpl(
    private val postRedisTemplateRepository: PostRedisTemplateRepository,
) : PostCacheWriteService {

    override fun createPostCache(dto: PostCacheCreateDto): PostCacheDto {
        val postCache = PostCache(postId = dto.postId, memberId = dto.memberId)
        val postKey: String = postCache.getPostKey()
        val postExpirationTimeMs: Long = postCache.getExpirationTimeMs()
        val postCacheDto: PostCacheDto = dto.toPostCacheDto()
        val postUpdateKey: String = postCache.getPostUpdateKey()
        val postUpdateExpirationTimeMs: Long = postCache.getPostUpdateExpirationTimeMs()
        // Post 캐시
        postRedisTemplateRepository.set(key = postKey, value = postCacheDto, expirationTimeMs = postExpirationTimeMs)
        // Post 수정 여부 캐시
        postRedisTemplateRepository.set(
            key = postUpdateKey,
            value = true,
            expirationTimeMs = postUpdateExpirationTimeMs,
        )
        return postCacheDto
    }
}
