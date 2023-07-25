package kr.co.hyo.domain.member.service

import kr.co.hyo.domain.member.dto.MemberFollowDto

interface MemberFollowWriteService {
    fun createMemberFollow(followingId: Long, followerId: Long): MemberFollowDto
}
