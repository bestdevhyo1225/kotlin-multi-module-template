package kr.co.hyo.domain.post.service

import kr.co.hyo.domain.post.dto.PostDto
import java.time.LocalDateTime

interface PostReadService {
    fun findPost(postId: Long): PostDto
    fun findPosts(postIds: List<Long>): List<PostDto>
    fun findPosts(memberIds: List<Long>, timelineUpdatedDatetime: LocalDateTime?): List<PostDto>
    fun findPostLikeCount(postId: Long): Long
    fun findPostViewCount(postId: Long): Long
}
