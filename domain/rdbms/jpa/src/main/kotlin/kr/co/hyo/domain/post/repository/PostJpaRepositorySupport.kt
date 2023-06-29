package kr.co.hyo.domain.post.repository

import kr.co.hyo.domain.post.entity.Post

interface PostJpaRepositorySupport {
    fun findByMemberIdAndId(memberId: Long, id: Long): Post
    fun findAllByMemberIdAndId(memberId: Long, lastId: Long, limit: Long): List<Post>
}
