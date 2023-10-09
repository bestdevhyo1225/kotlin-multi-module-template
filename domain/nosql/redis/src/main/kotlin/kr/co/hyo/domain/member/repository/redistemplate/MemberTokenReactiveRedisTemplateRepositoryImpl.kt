package kr.co.hyo.domain.member.repository.redistemplate

import kr.co.hyo.domain.member.repository.MemberTokenReactiveRedisTemplateRepository
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class MemberTokenReactiveRedisTemplateRepositoryImpl(
    private val reactiveRedisTemplate: ReactiveRedisTemplate<String, String>,
) : MemberTokenReactiveRedisTemplateRepository {

    override fun find(key: String): Mono<String> = reactiveRedisTemplate.opsForValue().get(key).map { it ?: "" }
}
