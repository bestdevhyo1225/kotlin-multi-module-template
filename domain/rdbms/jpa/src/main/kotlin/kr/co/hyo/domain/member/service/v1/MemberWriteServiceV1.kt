package kr.co.hyo.domain.member.service.v1

import kr.co.hyo.domain.member.dto.MemberCreateDto
import kr.co.hyo.domain.member.dto.MemberDto
import kr.co.hyo.domain.member.entity.Member
import kr.co.hyo.domain.member.mapper.MemberDtoMapper
import kr.co.hyo.domain.member.mapper.MemberEntityMapper
import kr.co.hyo.domain.member.repository.MemberRepository
import kr.co.hyo.domain.member.service.MemberWriteService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MemberWriteServiceV1(
    private val memberRepository: MemberRepository,
) : MemberWriteService {

    override fun createMember(dto: MemberCreateDto): MemberDto {
        val member: Member = MemberEntityMapper.toEntity(dto = dto)
        memberRepository.save(member)
        return MemberDtoMapper.toDto(member = member)
    }

    override fun changePassword(id: Long, oldPassword: String, newPassword: String) {
        val member = findById(id = id)
        member.changePassword(oldPassword = oldPassword, newPassword = newPassword)
    }

    override fun changeEmail(id: Long, email: String) {
        val member = findById(id = id)
        member.changeEmail(email = email)
    }

    private fun findById(id: Long): Member =
        memberRepository.findByIdOrNull(id = id) ?: throw NoSuchElementException("회원이 존재하지 않습니다.")
}
