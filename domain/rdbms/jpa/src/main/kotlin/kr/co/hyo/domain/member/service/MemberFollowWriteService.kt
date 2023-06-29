package kr.co.hyo.domain.member.service

import kr.co.hyo.domain.member.dto.MemberFollowDto

interface MemberFollowWriteService {
    fun create(followingId: Long, followerId: Long): MemberFollowDto
}
