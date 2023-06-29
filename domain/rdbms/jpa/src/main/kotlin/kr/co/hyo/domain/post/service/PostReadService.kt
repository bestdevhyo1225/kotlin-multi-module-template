package kr.co.hyo.domain.post.service

import kr.co.hyo.domain.post.dto.PostDto
import java.time.LocalDateTime

interface PostReadService {
    fun findPost(id: Long): PostDto
    fun findPost(memberId: Long, id: Long): PostDto
    fun findPosts(postIds: List<Long>): List<PostDto>
    fun findPosts(memberIds: List<Long>, timelineUpdatedDatetime: LocalDateTime?): List<PostDto>
}
