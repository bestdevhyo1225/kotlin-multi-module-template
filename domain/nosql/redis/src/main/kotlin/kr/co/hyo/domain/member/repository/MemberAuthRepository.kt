package kr.co.hyo.domain.member.repository

interface MemberAuthRepository {
    fun create(key: String, value: String, expirationTimeMs: Long)
    fun delete(key: String)
    fun find(key: String): String?
}
