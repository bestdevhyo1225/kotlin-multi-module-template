package kr.co.hyo.domain.chatting.repository

interface ChattingRedisTemplateRepository {
    fun <T : Any> executeWithTrascation(func: () -> T): T?
    fun <T : Any> get(key: String, clazz: Class<T>): T?
    fun increment(key: String): Long
    fun llen(key: String): Long
    fun <T : Any> lpush(key: String, value: T)
    fun <T : Any> lrange(key: String, start: Long, end: Long, clazz: Class<T>): List<T>
    fun <T : Any> mget(keys: List<String>, clazz: Class<T>): List<T>
    fun <T : Any> sadd(key: String, value: T)
    fun scard(key: String): Long
    fun <T : Any> set(key: String, value: T)
    fun <T : Any> zadd(key: String, value: T, score: Double)
    fun zcard(key: String): Long
    fun <T : Any> zrevRange(key: String, start: Long, end: Long, clazz: Class<T>): List<T>
    fun zremRangeByRank(key: String, start: Long, end: Long)
}
