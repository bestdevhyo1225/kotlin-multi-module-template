package kr.co.hyo.domain.member.service

interface MemberAuthWriteService {
    fun createBlackListToken(memberId: Long, accessToken: String, tokenExpirationTimeMs: Long)
    fun createRefreshToken(memberId: Long, refreshToken: String, expirationTimeMs: Long)
}
