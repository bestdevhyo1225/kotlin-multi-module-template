package kr.co.hyo.domain.member.repository.redistemplate

import kr.co.hyo.domain.member.repository.MemberAuthRedisTemplateRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit.MILLISECONDS

@Repository
class MemberAuthRedisTemplateRepositoryImpl(
    private val redisTemplate: RedisTemplate<String, String>,
) : MemberAuthRedisTemplateRepository {

    override fun create(key: String, value: String, expirationTimeMs: Long) {
        redisTemplate.opsForValue().set(key, value, expirationTimeMs, MILLISECONDS)
    }

    override fun delete(key: String) {
        redisTemplate.delete(key)
    }

    override fun find(key: String): String? {
        return redisTemplate.opsForValue().get(key)
    }
}
