package kr.co.hyo.domain.post.entity

class PostLike(
    val postId: Long,
) {

    fun getPostLikeMembersKey(): String = "post:$postId:like:members"
}
