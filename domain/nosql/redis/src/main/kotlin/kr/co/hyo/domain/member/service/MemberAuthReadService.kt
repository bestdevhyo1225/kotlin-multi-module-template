package kr.co.hyo.domain.member.service

interface MemberAuthReadService {
    fun verifyBlackListToken(memberId: Long, accessToken: String)
    fun verifyRefreshToken(memberId: Long, refreshToken: String)
}
