package kr.co.hyo.domain.post.repository

interface PostFeedRedisTemplateRepository {
    fun <T : Any> zadd(key: String, value: T, score: Double)
    fun zremRangeByRank(key: String, start: Long, end: Long)
    fun <T : Any> zrevRange(key: String, start: Long, end: Long, clazz: Class<T>): List<T>
}
