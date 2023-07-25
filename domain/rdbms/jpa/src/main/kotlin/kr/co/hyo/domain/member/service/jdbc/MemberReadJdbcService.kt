package kr.co.hyo.domain.member.service.jdbc

import kr.co.hyo.domain.member.dto.MemberDto
import kr.co.hyo.domain.member.dto.MemberAuthDto
import kr.co.hyo.domain.member.repository.MemberJdbcRepository
import kr.co.hyo.domain.member.service.MemberReadService
import org.springframework.transaction.annotation.Transactional

//@Service
@Transactional(readOnly = true)
class MemberReadJdbcService(
    private val memberJdbcRepository: MemberJdbcRepository,
) : MemberReadService {

    override fun findMember(memberId: Long): MemberDto {
        TODO("Not yet implemented")
    }

    override fun isCanNotMemberFanoutMaxLimit(memberId: Long): Boolean {
        TODO("Not yet implemented")
    }

    override fun verifyMember(loginId: String, password: String): MemberAuthDto {
        TODO("Not yet implemented")
    }

    override fun verifyMember(memberId: Long): MemberAuthDto {
        TODO("Not yet implemented")
    }


}
