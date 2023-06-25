package kr.co.hyo.common.util.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm.HS256
import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets
import java.security.Key
import java.util.Date

class JwtCreateHelper(
    private val secretKey: String,
    private val expirationTimeMs: Long,
    private val refreshExpirationTimeMs: Long,
) {

    fun createAccessToken(claims: Map<String, Any>): String {
        val currentDate = Date()
        val expirationDate = Date(currentDate.time + this.expirationTimeMs)
        return Jwts.builder()
            .setHeader(getHeader()) // Headers 설정
            .setClaims(claims) // Claims 설정
            .setIssuedAt(currentDate) // 발행 시간
            .setExpiration(expirationDate) // 토큰 만료 시간 설정
            .signWith(getKey(), HS256)
            .compact() // 토큰 생성
    }

    fun createRefreshToken(claims: Map<String, Any>): String {
        val currentDate = Date()
        val expirationDate = Date(currentDate.time + this.refreshExpirationTimeMs)
        return Jwts.builder()
            .setHeader(getHeader()) // Headers 설정
            .setClaims(claims) // Claims 설정
            .setIssuedAt(currentDate) // 발행 시간
            .setExpiration(expirationDate) // 토큰 만료 시간 설정
            .signWith(getKey(), HS256)
            .compact() // 토큰 생성
    }

    fun getRefreshExpirationTimeMs(): Long = this.refreshExpirationTimeMs

    private fun getHeader(): Map<String, String> = mapOf("typ" to "JWT", "alg" to "HS256")

    private fun getKey(): Key = Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))
}
