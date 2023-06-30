package kr.co.hyo.domain.post.entity

class PostCache(
    private val postId: Long,
) {

    fun getPostKey(): String = "post:$postId"
    fun getExpirationTimeMs(): Long = 60 * 60 * 3L // 3시간 (SECONDS 기준)
}
