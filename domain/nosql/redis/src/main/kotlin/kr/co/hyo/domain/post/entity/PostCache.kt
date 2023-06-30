package kr.co.hyo.domain.post.entity

class PostCache(
    val postId: Long,
) {

    fun getPostKey(): String = "post:$postId"
    fun getPostLikeCountKey(): String = "post:$postId:like:count"
    fun getPostViewCountKey(): String = "post:$postId:view:count"
    fun getExpirationTimeMs(): Long = 60 * 60 * 3L // 3시간 (SECONDS 기준)

    fun getPostKeyAndExpirationTimeMs(): Pair<String, Long> =
        Pair(first = getPostKey(), second = getExpirationTimeMs())
}
