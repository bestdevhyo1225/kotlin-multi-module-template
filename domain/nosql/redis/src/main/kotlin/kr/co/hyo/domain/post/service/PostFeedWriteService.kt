package kr.co.hyo.domain.post.service

interface PostFeedWriteService {
    fun create(memberId: Long, postId: Long)
}
