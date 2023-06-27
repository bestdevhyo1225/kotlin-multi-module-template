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

    override fun find(id: Long): MemberDto {
        TODO("Not yet implemented")
    }

    override fun verify(loginId: String, password: String): MemberAuthDto {
        TODO("Not yet implemented")
    }
}
