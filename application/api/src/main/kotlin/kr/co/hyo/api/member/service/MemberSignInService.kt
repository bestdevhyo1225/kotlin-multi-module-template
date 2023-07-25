package kr.co.hyo.api.member.service

import kr.co.hyo.common.util.jwt.JwtCreateHelper
import kr.co.hyo.domain.member.dto.MemberAuthDto
import kr.co.hyo.domain.member.service.MemberTokenReadService
import kr.co.hyo.domain.member.service.MemberTokenWriteService
import kr.co.hyo.domain.member.service.MemberReadService
import org.springframework.stereotype.Service

@Service
class MemberSignInService(
    private val memberTokenReadService: MemberTokenReadService,
    private val memberTokenWriteService: MemberTokenWriteService,
    private val memberReadService: MemberReadService,
    private val jwtCreateHelper: JwtCreateHelper,
) {

    fun verify(loginId: String, password: String): Map<String, Any> {
        val dto: MemberAuthDto = memberReadService.verifyMember(loginId = loginId, password = password)
        val accessToken: String = jwtCreateHelper.createAccessToken(claims = dto.jwtClaims)
        val refreshToken: String = jwtCreateHelper.createRefreshToken(claims = dto.jwtClaims)
        memberTokenWriteService.createRefreshToken(
            memberId = dto.id,
            refreshToken = refreshToken,
            expirationTimeMs = jwtCreateHelper.getRefreshExpirationTimeMs(),
        )
        return mapOf("accessToken" to accessToken, "refreshToken" to refreshToken)
    }

    fun refresh(memberId: Long, refreshToken: String): Map<String, Any> {
        memberTokenReadService.verifyRefreshToken(memberId = memberId, refreshToken = refreshToken)
        val dto: MemberAuthDto = memberReadService.verifyMember(memberId = memberId)
        val accessToken: String = jwtCreateHelper.createAccessToken(claims = dto.jwtClaims)
        return mapOf("accessToken" to accessToken)
    }
}
