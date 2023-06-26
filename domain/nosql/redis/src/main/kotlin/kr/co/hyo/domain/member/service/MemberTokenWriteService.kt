package kr.co.hyo.domain.member.service

interface MemberTokenWriteService {
    fun createBlackListToken(memberId: Long, accessToken: String, tokenExpirationTimeMs: Long)
    fun createRefreshToken(memberId: Long, refreshToken: String, expirationTimeMs: Long)
}
