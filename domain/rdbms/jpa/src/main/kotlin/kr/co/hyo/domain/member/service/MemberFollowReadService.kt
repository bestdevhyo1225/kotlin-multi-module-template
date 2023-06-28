package kr.co.hyo.domain.member.service

import kr.co.hyo.domain.member.dto.MemberFollowDto

interface MemberFollowReadService {
    fun findFollowers(memberId: Long, lastFollowerId: Long): List<MemberFollowDto>
}
