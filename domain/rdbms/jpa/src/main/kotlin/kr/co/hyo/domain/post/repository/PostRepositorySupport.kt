package kr.co.hyo.domain.post.repository

import kr.co.hyo.domain.post.entity.Post
import java.time.LocalDateTime

interface PostRepositorySupport {
    fun find(id: Long, memberId: Long): Post
    fun findAll(ids: List<Long>): List<Post>
    fun findAll(keyword: String, offset: Long, limit: Long): List<Post>
    fun findIds(memberIds: List<Long>, timelineUpdatedDatetime: LocalDateTime?, lastId: Long, limit: Long): List<Long>
}
