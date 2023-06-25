package kr.co.hyo.domain.member.service.redistemplate

import kr.co.hyo.domain.member.entity.MemberAuth
import kr.co.hyo.domain.member.repository.MemberAuthRedisTemplateRepository
import kr.co.hyo.domain.member.service.MemberAuthWriteService
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.Date

@Service
class MemberAuthWriteRedisTemplateService(
    private val memberAuthRedisTemplateRepository: MemberAuthRedisTemplateRepository,
) : MemberAuthWriteService {

    private val kotlinLogger = KotlinLogging.logger {}

    override fun createBlackListToken(memberId: Long, accessToken: String, tokenExpirationTimeMs: Long) {
        val memberAuth = MemberAuth(memberId = memberId)
        val refreshTokenKey: String = memberAuth.getRefreshTokenKey()
        val blackListTokenkey: String = memberAuth.getBlackListTokenKey()
        val expirationTimeMs: Long = tokenExpirationTimeMs - Date().time

        kotlinLogger.info { "tokenExpirationTimeMs: $tokenExpirationTimeMs, expirationTimeMs: $expirationTimeMs" }

        memberAuthRedisTemplateRepository.delete(key = refreshTokenKey)
        memberAuthRedisTemplateRepository.create(key = blackListTokenkey, value = accessToken, expirationTimeMs = expirationTimeMs)
    }

    override fun createRefreshToken(memberId: Long, refreshToken: String, expirationTimeMs: Long) {
        val memberAuth = MemberAuth(memberId = memberId)
        val key: String = memberAuth.getRefreshTokenKey()
        memberAuthRedisTemplateRepository.create(key = key, value = refreshToken, expirationTimeMs = expirationTimeMs)
    }
}
