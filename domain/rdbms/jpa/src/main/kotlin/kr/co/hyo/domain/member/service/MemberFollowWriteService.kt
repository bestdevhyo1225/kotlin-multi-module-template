package kr.co.hyo.domain.member.service

interface MemberFollowWriteService {
    fun create(memberId: Long, followerId: Long)
}
