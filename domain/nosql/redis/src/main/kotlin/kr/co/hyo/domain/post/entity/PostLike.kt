package kr.co.hyo.domain.post.entity

class PostLike(
    private val postId: Long,
) {

    fun getPostLikeMembersKey(): String = "post:$postId:like:members"
}
