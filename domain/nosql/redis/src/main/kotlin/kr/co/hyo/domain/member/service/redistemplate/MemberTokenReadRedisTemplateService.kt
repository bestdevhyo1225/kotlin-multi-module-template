package kr.co.hyo.domain.member.service.redistemplate

import kr.co.hyo.domain.member.entity.MemberToken
import kr.co.hyo.domain.member.repository.MemberTokenRedisTemplateRepository
import kr.co.hyo.domain.member.service.MemberTokenReadService
import org.springframework.stereotype.Service

@Service
class MemberTokenReadRedisTemplateService(
    private val memberTokenRedisTemplateRepository: MemberTokenRedisTemplateRepository,
) : MemberTokenReadService {

    override fun verifyBlackListToken(memberId: Long, accessToken: String) {
        val memberToken = MemberToken(memberId = memberId)
        val key: String = memberToken.getBlackListTokenKey()
        val value: String = memberTokenRedisTemplateRepository.find(key = key) ?: ""
        if (value.isNotBlank() && value == accessToken) {
            throw IllegalArgumentException("BlackList 토큰입니다")
        }
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
