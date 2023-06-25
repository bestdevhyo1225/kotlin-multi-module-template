package kr.co.hyo.domain.member.service.jdbc

import kr.co.hyo.domain.member.dto.MemberDto
import kr.co.hyo.domain.member.dto.MemberVerifyDto
import kr.co.hyo.domain.member.service.MemberReadService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

//@Service
@Transactional(readOnly = true)
class MemberReadJdbcService : MemberReadService {

    override fun find(id: Long): MemberDto {
        TODO("Not yet implemented")
    }

    override fun verify(loginId: String, password: String): MemberVerifyDto {
        TODO("Not yet implemented")
    }
}
