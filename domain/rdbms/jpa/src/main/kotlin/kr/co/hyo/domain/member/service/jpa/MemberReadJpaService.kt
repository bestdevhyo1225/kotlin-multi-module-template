package kr.co.hyo.domain.member.service.jpa

import kr.co.hyo.domain.member.dto.MemberDto
import kr.co.hyo.domain.member.dto.MemberAuthDto
import kr.co.hyo.domain.member.entity.Member
import kr.co.hyo.domain.member.mapper.MemberAuthDtoMapper
import kr.co.hyo.domain.member.mapper.MemberDtoMapper
import kr.co.hyo.domain.member.repository.MemberJpaRepositorySupport
import kr.co.hyo.domain.member.service.MemberReadService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberReadJpaService(
    private val memberJpaRepositorySupport: MemberJpaRepositorySupport,
) : MemberReadService {

    override fun findMember(memberId: Long): MemberDto {
        val member: Member = memberJpaRepositorySupport.find(id = memberId)
        return MemberDtoMapper.toDto(member = member)
    }

    override fun isCanNotMemberFanoutMaxLimit(memberId: Long): Boolean {
        val member: Member = memberJpaRepositorySupport.find(id = memberId)
        return member.followCount == 0L || member.followCount > Member.MEMBER_FANOUT_MAX_LIMIT
    }

    override fun verifyMember(loginId: String, password: String): MemberAuthDto {
        val member: Member = memberJpaRepositorySupport.find(loginId = loginId)
            ?: throw NoSuchElementException("회원이 존재하지 않습니다.")
        member.verifyPassword(password = password)
        return MemberAuthDtoMapper.toDto(member = member)
    }

    override fun verifyMember(memberId: Long): MemberAuthDto {
        val member: Member = memberJpaRepositorySupport.find(id = memberId)
        return MemberAuthDtoMapper.toDto(member = member)
    }
}
