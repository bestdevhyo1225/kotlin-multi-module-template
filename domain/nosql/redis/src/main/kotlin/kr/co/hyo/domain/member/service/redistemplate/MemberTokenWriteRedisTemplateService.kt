package kr.co.hyo.domain.member.service.redistemplate

import kr.co.hyo.domain.member.entity.MemberToken
import kr.co.hyo.domain.member.repository.MemberTokenRedisTemplateRepository
import kr.co.hyo.domain.member.service.MemberTokenWriteService
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.Date

@Service
class MemberTokenWriteRedisTemplateService(
    private val memberTokenRedisTemplateRepository: MemberTokenRedisTemplateRepository,
) : MemberTokenWriteService {

    private val kotlinLogger = KotlinLogging.logger {}

    override fun createBlackListToken(memberId: Long, accessToken: String, tokenExpirationTimeMs: Long) {
        val memberToken = MemberToken(memberId = memberId)
        val refreshTokenKey: String = memberToken.getRefreshTokenKey()
        val blackListTokenkey: String = memberToken.getBlackListTokenKey()
        val expirationTimeMs: Long = tokenExpirationTimeMs - Date().time

        kotlinLogger.info { "tokenExpirationTimeMs: $tokenExpirationTimeMs, expirationTimeMs: $expirationTimeMs" }

        memberTokenRedisTemplateRepository.delete(key = refreshTokenKey)
        memberTokenRedisTemplateRepository.create(key = blackListTokenkey, value = accessToken, expirationTimeMs = expirationTimeMs)
    }

    override fun createRefreshToken(memberId: Long, refreshToken: String, expirationTimeMs: Long) {
        val memberToken = MemberToken(memberId = memberId)
        val key: String = memberToken.getRefreshTokenKey()
        memberTokenRedisTemplateRepository.create(key = key, value = refreshToken, expirationTimeMs = expirationTimeMs)
    }
}
