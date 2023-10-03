package kr.co.hyo.domain.post.entity

class PostCache(
    private val postId: Long,
    private val memberId: Long,
) {

    fun getPostKey(): String = "post:$postId:member:$memberId"
    fun getPostUpdateKey(): String = "post:$postId:update"
    fun getExpirationTimeMs(): Long = 60 * 60 * 3L // 3시간 (SECONDS 기준)
    fun getPostUpdateExpirationTimeMs(): Long = 60 // 1분 (SECONDS 기준)

    companion object {
        fun getPostUpdateKey(postId: Long): String = "post:$postId:update"
    }
}
