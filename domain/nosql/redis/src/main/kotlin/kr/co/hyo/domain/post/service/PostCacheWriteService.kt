package kr.co.hyo.domain.post.service

import kr.co.hyo.domain.post.dto.PostCacheCreateDto

interface PostCacheWriteService {
    fun create(dto: PostCacheCreateDto)
    fun createLikeCount(postId: Long, postLikeCount: Long)
    fun createViewCount(postId: Long, postViewCount: Long)
}
