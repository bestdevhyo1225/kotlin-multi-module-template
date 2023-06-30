package kr.co.hyo.domain.post.service

interface PostViewWriteService {
    fun increment(postId: Long, postOwnMemberId: Long, memberId: Long): Long
}
