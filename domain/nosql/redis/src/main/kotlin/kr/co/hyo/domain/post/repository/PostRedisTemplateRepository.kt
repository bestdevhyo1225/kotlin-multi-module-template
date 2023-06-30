package kr.co.hyo.domain.post.repository

interface PostRedisTemplateRepository {
    fun <T : Any> get(key: String, clazz: Class<T>): T?
    fun <T : Any> set(key: String, value: T, expirationTimeMs: Long)
    fun <T : Any> zadd(key: String, value: T, score: Double)
    fun zremRangeByRank(key: String, start: Long, end: Long)
    fun <T : Any> zrevRange(key: String, start: Long, end: Long, clazz: Class<T>): List<T>
}
