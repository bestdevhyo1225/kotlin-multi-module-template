package kr.co.hyo.domain.post.service

import kr.co.hyo.domain.post.dto.PostDto
import java.time.LocalDateTime

interface PostReadService {
    fun findPost(postId: Long): PostDto
    fun findPosts(postIds: List<Long>): List<PostDto>
    fun findPosts(keyword: String, offset: Long, limit: Long): List<PostDto>
    fun findPostIds(
        memberIds: List<Long>,
        timelineUpdatedDatetime: LocalDateTime?,
        lastPostId: Long,
        limit: Long,
    ): List<Long>
}
