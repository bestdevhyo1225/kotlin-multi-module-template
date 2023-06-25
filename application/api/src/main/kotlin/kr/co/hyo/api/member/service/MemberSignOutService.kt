package kr.co.hyo.api.member.service

import kr.co.hyo.common.util.jwt.JwtParseHelper
import kr.co.hyo.domain.member.service.MemberAuthWriteService
import org.springframework.stereotype.Service

@Service
class MemberSignOutService(
    private val memberAuthWriteService: MemberAuthWriteService,
    private val jwtParseHelper: JwtParseHelper,
) {

    fun out(memberId: Long, accessToken: String) {
        val tokenExpirationTimeMs: Long = jwtParseHelper.getExpirationTimeMs(accessToken = accessToken)
        memberAuthWriteService.createBlackListToken(
            memberId = memberId,
            accessToken = accessToken,
            tokenExpirationTimeMs = tokenExpirationTimeMs,
        )
    }
}
