package kr.co.hyo.domain.member.entity

class MemberAuth(
    private val memberId: Long,
) {

    fun getRefreshTokenKey(): String {
        return "auth:${this.memberId}:refresh-token"
    }

    fun getBlackListTokenKey(): String {
        return "auth:${this.memberId}:blacklist-token"
    }
}
