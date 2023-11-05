package kr.co.hyo.domain.chatting.repository.redistemplate

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kr.co.hyo.domain.chatting.repository.ChattingRedisTemplateRepository
import mu.KotlinLogging
import org.springframework.data.redis.connection.RedisConnection
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class ChattingRedisTemplateRepositoryImpl(
    private val redisTemplate: RedisTemplate<String, String>,
) : ChattingRedisTemplateRepository {

    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())
    private val logger = KotlinLogging.logger {}

    override fun <T : Any> executeWithTrascation(func: () -> T): T? {
        var result: T? = null
        redisTemplate.execute { redisConnection: RedisConnection ->
            try {
                logger.info { "Start transaction" }
                redisConnection.multi()

                // 명령어 실행
                result = func()

                redisConnection.exec()
                logger.info { "Exec transaction (Commit)" }
            } catch (exception: RuntimeException) { // RedisConnectionFailureException, QueryTimeoutException 포함
                redisConnection.discard()
                logger.info { "Discard transaction (Rollback)" }
            }
        }
        return result
    }

    override fun <T : Any> get(key: String, clazz: Class<T>): T? {
        return redisTemplate
            .opsForValue()
            .get(key)?.let { jacksonObjectMapper.readValue(it, clazz) }
    }

    override fun increment(key: String): Long {
        return redisTemplate
            .opsForValue()
            .increment(key) ?: throw RuntimeException("유효하지 않은 null 값을 반환했습니다.")
    }

    override fun llen(key: String): Long =
        redisTemplate
            .opsForList()
            .size(key) ?: 0L

    override fun <T : Any> lpush(key: String, value: T) {
        redisTemplate
            .opsForList()
            .leftPush(key, jacksonObjectMapper.writeValueAsString(value))
    }

    override fun <T : Any> lrange(key: String, start: Long, end: Long, clazz: Class<T>): List<T> {
        val values: List<String>? = redisTemplate
            .opsForList()
            .range(key, start, end)

        if (values.isNullOrEmpty()) {
            return listOf()
        }

        return values.map { jacksonObjectMapper.readValue(it, clazz)  }
    }

    override fun <T : Any> mget(keys: List<String>, clazz: Class<T>): List<T> {
        val values: List<String>? = redisTemplate
            .opsForValue()
            .multiGet(keys)

        if (values.isNullOrEmpty()) {
            return listOf()
        }

        return values.map { jacksonObjectMapper.readValue(it, clazz)  }
    }

    override fun <T : Any> sadd(key: String, value: T) {
        redisTemplate
            .opsForSet()
            .add(key, jacksonObjectMapper.writeValueAsString(value))
    }

    override fun scard(key: String): Long = redisTemplate.opsForSet().size(key) ?: 0L

    override fun <T : Any> set(key: String, value: T) {
        redisTemplate
            .opsForValue()
            .set(key, jacksonObjectMapper.writeValueAsString(value))
    }

    override fun <T : Any> zadd(key: String, value: T, score: Double) {
        redisTemplate
            .opsForZSet()
            .add(key, jacksonObjectMapper.writeValueAsString(value), score)
    }

    override fun zcard(key: String): Long = redisTemplate.opsForZSet().size(key) ?: 0L

    override fun <T : Any> zrevRange(key: String, start: Long, end: Long, clazz: Class<T>): List<T> {
        val values: Set<String?>? = redisTemplate
            .opsForZSet()
            .reverseRange(key, start, end)

        if (values.isNullOrEmpty()) {
            return listOf()
        }

        return values.map { jacksonObjectMapper.readValue(it, clazz) }
    }

    override fun zremRangeByRank(key: String, start: Long, end: Long) {
        redisTemplate
            .opsForZSet()
            .removeRange(key, start, end)
    }
}
