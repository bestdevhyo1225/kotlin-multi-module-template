package kr.co.hyo.domain.member.service

import reactor.core.publisher.Mono

interface MemberTokenReadService {
    fun verifyBlackListToken(memberId: Long, accessToken: String): Mono<Void>
    fun verifyRefreshToken(memberId: Long, refreshToken: String)
}
