package kr.co.hyo.domain.member.service.v1

import kr.co.hyo.domain.member.entity.MemberAuth
import kr.co.hyo.domain.member.repository.MemberAuthRepository
import kr.co.hyo.domain.member.service.MemberAuthWriteService
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.Date

@Service
class MemberAuthWriteServiceV1(
    private val memberAuthRepository: MemberAuthRepository,
) : MemberAuthWriteService {

    private val kotlinLogger = KotlinLogging.logger {}

    override fun createBlackListToken(memberId: Long, accessToken: String, tokenExpirationTimeMs: Long) {
        val memberAuth = MemberAuth(memberId = memberId)
        val refreshTokenKey: String = memberAuth.getRefreshTokenKey()
        val blackListTokenkey: String = memberAuth.getBlackListTokenKey()
        val expirationTimeMs: Long = tokenExpirationTimeMs - Date().time

        kotlinLogger.info { "tokenExpirationTimeMs: $tokenExpirationTimeMs, expirationTimeMs: $expirationTimeMs" }

        memberAuthRepository.delete(key = refreshTokenKey)
        memberAuthRepository.create(key = blackListTokenkey, value = accessToken, expirationTimeMs = expirationTimeMs)
    }

    override fun createRefreshToken(memberId: Long, refreshToken: String, expirationTimeMs: Long) {
        val memberAuth = MemberAuth(memberId = memberId)
        val key: String = memberAuth.getRefreshTokenKey()
        memberAuthRepository.create(key = key, value = refreshToken, expirationTimeMs = expirationTimeMs)
    }
}
