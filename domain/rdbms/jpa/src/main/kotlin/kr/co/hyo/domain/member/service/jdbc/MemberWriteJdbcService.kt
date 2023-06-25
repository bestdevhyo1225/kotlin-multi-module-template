package kr.co.hyo.domain.member.service.jdbc

import kr.co.hyo.domain.member.dto.MemberCreateDto
import kr.co.hyo.domain.member.dto.MemberDto
import kr.co.hyo.domain.member.service.MemberWriteService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

//@Service
@Transactional
class MemberWriteJdbcService : MemberWriteService {

    override fun createMember(dto: MemberCreateDto): MemberDto {
        TODO("Not yet implemented")
    }

    override fun changePassword(id: Long, oldPassword: String, newPassword: String) {
        TODO("Not yet implemented")
    }

    override fun changeEmail(id: Long, email: String) {
        TODO("Not yet implemented")
    }
}
