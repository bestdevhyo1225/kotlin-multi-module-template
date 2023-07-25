package kr.co.hyo.domain.member.service.v1

import kr.co.hyo.domain.member.dto.MemberFollowDto
import kr.co.hyo.domain.member.entity.Member
import kr.co.hyo.domain.member.entity.MemberFollow
import kr.co.hyo.domain.member.mapper.MemberFollowDtoMapper
import kr.co.hyo.domain.member.repository.MemberFollowRepository
import kr.co.hyo.domain.member.repository.MemberRepository
import kr.co.hyo.domain.member.service.MemberFollowWriteService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MemberFollowWriteServiceV1(
    private val memberFollowRepository: MemberFollowRepository,
    private val memberRepository: MemberRepository,
) : MemberFollowWriteService {

    override fun createMemberFollow(followingId: Long, followerId: Long): MemberFollowDto {
        val memberFollowing: Member = memberRepository.findByIdOrNull(id = followingId)
            ?: throw NoSuchElementException("팔로잉 회원이 존재하지 않습니다.")
        memberFollowing.incrementFollowCount()

        val memberFollower: Member = memberRepository.findByIdOrNull(id = followerId)
            ?: throw NoSuchElementException("팔로우 회원이 존재하지 않습니다.")
        memberFollower.incrementFollowingCount()

        val memberFollow = MemberFollow(followingId = followingId, followerId = followerId)
        memberFollowRepository.save(memberFollow)
        return MemberFollowDtoMapper.toDto(memberFollow = memberFollow)
    }
}
