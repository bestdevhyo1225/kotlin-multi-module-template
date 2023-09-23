package kr.co.hyo.domain.post.entity

class PostCache(
    private val postId: Long,
    private val memberId: Long,
) {

    fun getPostKey(): String = "post:$postId:member:$memberId"
    fun getExpirationTimeMs(): Long = 60 * 60 * 3L // 3시간 (SECONDS 기준)
}
