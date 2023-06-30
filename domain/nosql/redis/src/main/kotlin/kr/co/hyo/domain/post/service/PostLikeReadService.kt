package kr.co.hyo.domain.post.service

interface PostLikeReadService {
    fun count(postId: Long): Long
}
