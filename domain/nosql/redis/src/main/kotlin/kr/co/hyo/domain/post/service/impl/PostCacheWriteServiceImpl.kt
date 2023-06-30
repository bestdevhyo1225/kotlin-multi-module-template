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

    override fun create(dto: PostCacheCreateDto): PostCacheDto {
        val postCache = PostCache(postId = dto.postId)
        val postKey: String = postCache.getPostKey()
        val expirationTimeMs: Long = postCache.getExpirationTimeMs()
        val postCacheDto: PostCacheDto = dto.toPostCacheDto()
        postRedisTemplateRepository.set(key = postKey, value = postCacheDto, expirationTimeMs = expirationTimeMs)
        return postCacheDto
    }
}
