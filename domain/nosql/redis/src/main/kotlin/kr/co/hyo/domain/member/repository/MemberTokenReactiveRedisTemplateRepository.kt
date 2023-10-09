package kr.co.hyo.domain.member.repository

import reactor.core.publisher.Mono

interface MemberTokenReactiveRedisTemplateRepository {
    fun find(key: String): Mono<String>
}
