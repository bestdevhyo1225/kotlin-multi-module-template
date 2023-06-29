package kr.co.hyo.domain.post.entity

class PostFeed(
    private val memberId: Long,
) {

    companion object {
        const val ZSET_POST_FEED_MAX_LIMIT = -501L
    }

    fun getMemberIdFeedsKey(): String = "member:$memberId:feeds"

    fun getMemberIdFeedsKeyAndExpireTime(): Pair<String, Long> =
        Pair(first = getMemberIdFeedsKey(), second = 60 * 60 * 24L) // 24시간
}
