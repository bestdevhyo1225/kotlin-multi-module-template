package kr.co.hyo.domain.post.service

interface PostFeedReadService {
    fun findPostIds(memberId: Long, start: Long, end: Long): List<Long>
}
