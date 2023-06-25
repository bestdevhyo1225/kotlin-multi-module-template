package kr.co.hyo.domain.member.service.v1

import kr.co.hyo.domain.member.dto.MemberDto
import kr.co.hyo.domain.member.dto.MemberVerifyDto
import kr.co.hyo.domain.member.entity.Member
import kr.co.hyo.domain.member.mapper.MemberDtoMapper
import kr.co.hyo.domain.member.repository.MemberRepositorySupport
import kr.co.hyo.domain.member.service.MemberReadService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberReadServiceV1(
    private val memberRepositorySupport: MemberRepositorySupport,
) : MemberReadService {

    override fun find(id: Long): MemberDto {
        val member: Member = memberRepositorySupport.findById(id = id)
        return MemberDtoMapper.toDto(member = member)
    }

    override fun verify(loginId: String, password: String): MemberVerifyDto {
        val member: Member = memberRepositorySupport.findByLoginId(loginId = loginId)
        member.verifyPassword(password = password)
        return MemberVerifyDto(id = member.id!!, jwtClaims = member.getJwtClaims())
    }
}
