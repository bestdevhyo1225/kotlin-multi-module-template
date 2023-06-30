package kr.co.hyo.domain.post.entity

class PostView(
    private val postId: Long,
    private val postOwnMemberId: Long,
    private val memberId: Long,
) {

    fun getPostViewCountKey(): String = "post:$postId:view:count"

    fun isOwn(): Boolean = postOwnMemberId == memberId
}
