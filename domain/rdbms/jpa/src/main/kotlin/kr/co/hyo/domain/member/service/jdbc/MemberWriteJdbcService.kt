package kr.co.hyo.domain.member.service.jdbc

import kr.co.hyo.domain.member.dto.MemberCreateDto
import kr.co.hyo.domain.member.dto.MemberDto
import kr.co.hyo.domain.member.repository.MemberJdbcRepository
import kr.co.hyo.domain.member.service.MemberWriteService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

//@Service
@Transactional
class MemberWriteJdbcService(
    private val memberJdbcRepository: MemberJdbcRepository,
) : MemberWriteService {

    override fun createMember(dto: MemberCreateDto): MemberDto {
        TODO("Not yet implemented")
    }

    override fun changeMemberPassword(memberId: Long, oldPassword: String, newPassword: String) {
        TODO("Not yet implemented")
    }

    override fun changeMemberEmail(memberId: Long, email: String) {
        TODO("Not yet implemented")
    }

    override fun changeMemberTimelineUpdatedDatetime(memberId: Long) {
        TODO("Not yet implemented")
    }


}
