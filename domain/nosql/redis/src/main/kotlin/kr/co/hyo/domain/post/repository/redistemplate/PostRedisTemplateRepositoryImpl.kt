package kr.co.hyo.domain.post.repository.redistemplate

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kr.co.hyo.domain.post.repository.PostRedisTemplateRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit.SECONDS

@Repository
class PostRedisTemplateRepositoryImpl(
    private val redisTemplate: RedisTemplate<String, String>,
) : PostRedisTemplateRepository {

    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    override fun <T : Any> get(key: String, clazz: Class<T>): T? {
        return redisTemplate.opsForValue().get(key)?.let { jacksonObjectMapper.readValue(it, clazz) }
    }

    override fun <T : Any> set(key: String, value: T, expirationTimeMs: Long) {
        redisTemplate.opsForValue()
            .set(key, jacksonObjectMapper.writeValueAsString(value), expirationTimeMs, SECONDS)
    }

    override fun <T : Any> zadd(key: String, value: T, score: Double) {
        redisTemplate.opsForZSet().add(key, jacksonObjectMapper.writeValueAsString(value), score)
    }

    override fun zremRangeByRank(key: String, start: Long, end: Long) {
        redisTemplate.opsForZSet().removeRange(key, start, end)
    }

    override fun <T : Any> zrevRange(key: String, start: Long, end: Long, clazz: Class<T>): List<T> {
        val values: Set<String?>? = redisTemplate.opsForZSet().reverseRange(key, start, end)
        if (values.isNullOrEmpty()) {
            return listOf()
        }
        return values.map { jacksonObjectMapper.readValue(it, clazz) }
    }
}
