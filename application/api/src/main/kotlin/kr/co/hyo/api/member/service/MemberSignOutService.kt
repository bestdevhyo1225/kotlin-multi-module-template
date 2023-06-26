package kr.co.hyo.api.member.service

import kr.co.hyo.common.util.jwt.JwtParseHelper
import kr.co.hyo.domain.member.service.MemberTokenWriteService
import org.springframework.stereotype.Service

@Service
class MemberSignOutService(
    private val memberTokenWriteService: MemberTokenWriteService,
    private val jwtParseHelper: JwtParseHelper,
) {

    fun out(memberId: Long, accessToken: String) {
        val tokenExpirationTimeMs: Long = jwtParseHelper.getExpirationTimeMs(accessToken = accessToken)
        memberTokenWriteService.createBlackListToken(
            memberId = memberId,
            accessToken = accessToken,
            tokenExpirationTimeMs = tokenExpirationTimeMs,
        )
    }
}
