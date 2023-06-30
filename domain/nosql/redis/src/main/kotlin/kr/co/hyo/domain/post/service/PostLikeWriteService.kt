package kr.co.hyo.domain.post.service

interface PostLikeWriteService {
    fun likePost(postId: Long, memberId: Long)
    fun deleteLikePost(postId: Long, memberId: Long)
}
