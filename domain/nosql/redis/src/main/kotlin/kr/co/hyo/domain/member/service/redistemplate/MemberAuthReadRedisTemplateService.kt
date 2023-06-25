package kr.co.hyo.domain.member.service.redistemplate

import kr.co.hyo.domain.member.entity.MemberAuth
import kr.co.hyo.domain.member.repository.MemberAuthRedisTemplateRepository
import kr.co.hyo.domain.member.service.MemberAuthReadService
import org.springframework.stereotype.Service

@Service
class MemberAuthReadRedisTemplateService(
    private val memberAuthRedisTemplateRepository: MemberAuthRedisTemplateRepository,
) : MemberAuthReadService {

    override fun verifyBlackListToken(memberId: Long, accessToken: String) {
        val memberAuth = MemberAuth(memberId = memberId)
        val key: String = memberAuth.getBlackListTokenKey()
        val value: String = memberAuthRedisTemplateRepository.find(key = key) ?: ""
        if (value.isNotBlank() && value == accessToken) {
            throw RuntimeException("BlackList 토큰입니다")
        }
    }

    override fun verifyRefreshToken(memberId: Long, refreshToken: String) {
        val memberAuth = MemberAuth(memberId = memberId)
        val key: String = memberAuth.getRefreshTokenKey()
        val value: String = memberAuthRedisTemplateRepository.find(key = key) ?: throw RuntimeException("Refresh 토큰이 존재하지 않습니다")
        if (value != refreshToken) {
            throw RuntimeException("Refresh 토큰이 일치하지 않습니다")
        }
    }
}
