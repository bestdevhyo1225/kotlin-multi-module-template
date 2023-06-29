package kr.co.hyo.domain.member.service

import kr.co.hyo.domain.member.dto.MemberFollowDto

interface MemberFollowReadService {
    fun findFollowers(followingId: Long, lastFollowerId: Long): List<MemberFollowDto>
    fun findFollowings(followerId: Long): List<MemberFollowDto>
}
