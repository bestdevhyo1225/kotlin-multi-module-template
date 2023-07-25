package kr.co.hyo.domain.post.repository

import kr.co.hyo.domain.post.entity.Post
import java.time.LocalDateTime

interface PostJpaRepositorySupport {
    fun find(id: Long): Post
    fun findAll(ids: List<Long>): List<Post>
    fun findIds(memberIds: List<Long>, timelineUpdatedDatetime: LocalDateTime?, lastId: Long, limit: Long): List<Long>
}
