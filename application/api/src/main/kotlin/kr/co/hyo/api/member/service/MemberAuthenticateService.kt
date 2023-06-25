package kr.co.hyo.api.member.service

import kr.co.hyo.common.util.jwt.JwtParseHelper
import kr.co.hyo.domain.member.service.MemberAuthReadService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Service

@Service
class MemberAuthenticateService(
    private val memberAuthReadService: MemberAuthReadService,
    private val jwtParseHelper: JwtParseHelper,
) {

    companion object {
        private const val MEMBER_ID = "memberId"
    }

    fun create(accessToken: String): UsernamePasswordAuthenticationToken {
        jwtParseHelper.verify(accessToken = accessToken)
        val memberId: Long = jwtParseHelper.getValue(accessToken, MEMBER_ID).toLong()
        val authorities: List<GrantedAuthority> = listOf()
        memberAuthReadService.verifyBlackListToken(memberId = memberId, accessToken = accessToken)
        return UsernamePasswordAuthenticationToken(memberId, accessToken, authorities)
    }
}
