package kr.co.hyo.domain.member.repository.jdbctemplate

import kr.co.hyo.domain.member.repository.MemberJdbcRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class MemberJdbcTemplateRepository : MemberJdbcRepository {
}
