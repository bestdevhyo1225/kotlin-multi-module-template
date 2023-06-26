package kr.co.hyo.domain.member.repository

interface MemberTokenRedisTemplateRepository {
    fun create(key: String, value: String, expirationTimeMs: Long)
    fun delete(key: String)
    fun find(key: String): String?
}
