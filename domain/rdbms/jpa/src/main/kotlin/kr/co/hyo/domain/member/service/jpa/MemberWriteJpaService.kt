package kr.co.hyo.domain.member.service.jpa

import kr.co.hyo.domain.member.dto.MemberCreateDto
import kr.co.hyo.domain.member.dto.MemberDto
import kr.co.hyo.domain.member.entity.Member
import kr.co.hyo.domain.member.mapper.MemberDtoMapper
import kr.co.hyo.domain.member.mapper.MemberEntityMapper
import kr.co.hyo.domain.member.repository.MemberJpaRepository
import kr.co.hyo.domain.member.repository.MemberJpaRepositorySupport
import kr.co.hyo.domain.member.service.MemberWriteService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MemberWriteJpaService(
    private val memberJpaRepository: MemberJpaRepository,
    private val memberJpaRepositorySupport: MemberJpaRepositorySupport,
) : MemberWriteService {

    override fun createMember(dto: MemberCreateDto): MemberDto {
        memberJpaRepositorySupport.find(loginId = dto.loginId)
            ?.let { throw IllegalArgumentException("로그인 아이디 '${it.loginId}' 는 이미 사용중입니다") }

        val member: Member = MemberEntityMapper.toEntity(dto = dto)
        memberJpaRepository.save(member)
        return MemberDtoMapper.toDto(member = member)
    }

    override fun changeMemberPassword(memberId: Long, oldPassword: String, newPassword: String) {
        val member = findById(id = memberId)
        member.changePassword(oldPassword = oldPassword, newPassword = newPassword)
    }

    override fun changeMemberEmail(memberId: Long, email: String) {
        val member = findById(id = memberId)
        member.changeEmail(email = email)
    }

    override fun changeMemberTimelineUpdatedDatetime(memberId: Long) {
        val member = findById(id = memberId)
        member.changeTimelineUpdatedDatetime()
    }

    private fun findById(id: Long): Member =
        memberJpaRepository.findByIdOrNull(id = id) ?: throw NoSuchElementException("회원이 존재하지 않습니다.")
}
