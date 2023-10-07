package kr.co.hyo.service.member

import kr.co.hyo.common.util.jwt.JwtParseHelper
import kr.co.hyo.domain.member.service.MemberTokenReadService
import org.springframework.stereotype.Service

@Service
class MemberAuthenticateService(
    private val jwtParseHelper: JwtParseHelper,
    private val memberTokenReadService: MemberTokenReadService,
) {

    companion object {
        private const val MEMBER_ID = "memberId"
    }

    fun verifyAccessToken(accessToken: String) {
        jwtParseHelper.verify(accessToken = accessToken)
        val memberId: Long = jwtParseHelper.getValue(accessToken = accessToken, key = MEMBER_ID).toLong()
        memberTokenReadService.verifyBlackListToken(memberId = memberId, accessToken = accessToken)
    }
}
