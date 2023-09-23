package kr.co.hyo.domain.post.service

import kr.co.hyo.domain.post.dto.PostCacheDto

interface PostCacheReadService {
    fun findPostCache(postId: Long, memberId: Long): PostCacheDto?
}
