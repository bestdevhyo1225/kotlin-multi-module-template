package kr.co.hyo.domain.post.service

import kr.co.hyo.domain.post.dto.PostCacheDto

interface PostCacheReadService {
    fun findPostCache(postId: Long): PostCacheDto?
    fun findPostCacheLikeCount(postId: Long): Long?
    fun findPostCacheViewCount(postId: Long): Long?
}
