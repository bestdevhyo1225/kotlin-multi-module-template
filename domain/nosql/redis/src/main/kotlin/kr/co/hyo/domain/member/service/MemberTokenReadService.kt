package kr.co.hyo.domain.member.service

interface MemberTokenReadService {
    fun verifyBlackListToken(memberId: Long, accessToken: String)
    fun verifyRefreshToken(memberId: Long, refreshToken: String)
}
