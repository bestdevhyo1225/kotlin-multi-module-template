package kr.co.hyo.domain.post.repository

import kr.co.hyo.domain.post.entity.Post
import java.time.LocalDateTime

interface PostJpaRepositorySupport {
    fun findById(id: Long): Post
    fun findAllByIds(ids: List<Long>): List<Post>
    fun findIds(memberIds: List<Long>, timelineUpdatedDatetime: LocalDateTime?, lastId: Long, limit: Long): List<Long>
}
