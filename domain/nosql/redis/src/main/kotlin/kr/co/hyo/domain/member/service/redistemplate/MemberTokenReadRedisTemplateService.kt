package kr.co.hyo.domain.member.service.redistemplate

import kr.co.hyo.domain.member.entity.MemberToken
import kr.co.hyo.domain.member.repository.MemberTokenReactiveRedisTemplateRepository
import kr.co.hyo.domain.member.repository.MemberTokenRedisTemplateRepository
import kr.co.hyo.domain.member.service.MemberTokenReadService
import mu.KotlinLogging
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class MemberTokenReadRedisTemplateService(
    private val memberTokenRedisTemplateRepository: MemberTokenRedisTemplateRepository,
    private val memberTokenReactiveRedisTemplateRepository: MemberTokenReactiveRedisTemplateRepository,
) : MemberTokenReadService {

    private val logger = KotlinLogging.logger {}

    override fun verifyBlackListToken(memberId: Long, accessToken: String): Mono<Void> {
        val memberToken = MemberToken(memberId = memberId)
        val key: String = memberToken.getBlackListTokenKey()
        return memberTokenReactiveRedisTemplateRepository.find(key = key)
            .log()
            .map {
                if (it.isNotBlank() && it == accessToken) {
                    throw IllegalArgumentException("블랙 리스트 토큰입니다")
                }
            }
            .then()
    }

    override fun verifyRefreshToken(memberId: Long, refreshToken: String) {
        val memberToken = MemberToken(memberId = memberId)
        val key: String = memberToken.getRefreshTokenKey()
        val value: String = memberTokenRedisTemplateRepository.find(key = key)
            ?: throw IllegalArgumentException("Refresh 토큰이 존재하지 않습니다")
        if (value != refreshToken) {
            throw IllegalArgumentException("Refresh 토큰이 일치하지 않습니다")
        }
    }
}
