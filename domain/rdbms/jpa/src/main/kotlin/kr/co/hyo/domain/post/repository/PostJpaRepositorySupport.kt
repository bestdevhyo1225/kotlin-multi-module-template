package kr.co.hyo.domain.post.repository

import kr.co.hyo.domain.post.entity.Post
import java.time.LocalDateTime

interface PostJpaRepositorySupport {
    fun findById(id: Long): Post
    fun findAllByIds(postIds: List<Long>): List<Post>
    fun findAllByMemberIdsAndCreatedDatetime(memberIds: List<Long>, timelineUpdatedDatetime: LocalDateTime?): List<Post>
}
