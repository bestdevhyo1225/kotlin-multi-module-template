package kr.co.hyo.domain.post.repository.redistemplate

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kr.co.hyo.domain.post.repository.PostFeedRedisTemplateRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class PostFeedRedisTemplateRepositoryImpl(
    private val redisTemplate: RedisTemplate<String, String>,
) : PostFeedRedisTemplateRepository {

    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

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
