package kr.co.hyo.domain.member.service.jpa

import kr.co.hyo.domain.member.dto.MemberFollowDto
import kr.co.hyo.domain.member.entity.Member
import kr.co.hyo.domain.member.entity.MemberFollow
import kr.co.hyo.domain.member.mapper.MemberFollowDtoMapper
import kr.co.hyo.domain.member.repository.MemberFollowJpaRepository
import kr.co.hyo.domain.member.repository.MemberJpaRepository
import kr.co.hyo.domain.member.service.MemberFollowWriteService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MemberFollowWriteJpaService(
    private val memberFollowJpaRepository: MemberFollowJpaRepository,
    private val memberJpaRepository: MemberJpaRepository,
) : MemberFollowWriteService {

    override fun create(followingId: Long, followerId: Long): MemberFollowDto {
        val memberFollowing: Member = memberJpaRepository.findByIdOrNull(id = followingId)
            ?: throw NoSuchElementException("팔로잉 회원이 존재하지 않습니다.")
        memberFollowing.incrementFollowCount()

        val memberFollower: Member = memberJpaRepository.findByIdOrNull(id = followerId)
            ?: throw NoSuchElementException("팔로우 회원이 존재하지 않습니다.")
        memberFollower.incrementFollowingCount()

        val memberFollow = MemberFollow(followingId = followingId, followerId = followerId)
        memberFollowJpaRepository.save(memberFollow)
        return MemberFollowDtoMapper.toDto(memberFollow = memberFollow)
    }
}
