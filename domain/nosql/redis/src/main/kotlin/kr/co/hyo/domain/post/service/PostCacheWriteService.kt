package kr.co.hyo.domain.post.service

import kr.co.hyo.domain.post.dto.PostCacheCreateDto
import kr.co.hyo.domain.post.dto.PostCacheDto

interface PostCacheWriteService {
    fun create(dto: PostCacheCreateDto): PostCacheDto
}
