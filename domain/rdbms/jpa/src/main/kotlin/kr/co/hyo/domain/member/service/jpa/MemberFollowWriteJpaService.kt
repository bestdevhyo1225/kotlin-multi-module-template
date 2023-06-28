package kr.co.hyo.domain.member.service.jpa

import kr.co.hyo.domain.member.entity.Member
import kr.co.hyo.domain.member.entity.MemberFollow
import kr.co.hyo.domain.member.repository.MemberFollowJpaRepository
import kr.co.hyo.domain.member.repository.MemberJpaRepository
import kr.co.hyo.domain.member.service.MemberFollowWriteService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MemberFollowWriteJpaService(
    private val memberJpaRepository: MemberJpaRepository,
    private val memberrFollowJpaRepository: MemberFollowJpaRepository,
) : MemberFollowWriteService {

    override fun create(memberId: Long, followerId: Long) {
        val member: Member = memberJpaRepository.findByIdOrNull(id = memberId)
            ?: throw NoSuchElementException("회원이 존재하지 않습니다.")
        val memberFollow = MemberFollow(member = member, followerId = followerId)
        memberrFollowJpaRepository.save(memberFollow)
    }
}
