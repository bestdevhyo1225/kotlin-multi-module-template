package kr.co.hyo.domain.member.repository.querydsl

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.hyo.domain.member.entity.Member
import kr.co.hyo.domain.member.entity.QMember.member
import kr.co.hyo.domain.member.repository.MemberRepositorySupport
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class MemberRepositoryQueryDslSupport(
    private val queryFactory: JPAQueryFactory,
) : MemberRepositorySupport {

    override fun findById(id: Long): Member {
        return queryFactory
            .selectFrom(member)
            .where(
                memberIdEq(id = id),
                memberDeletedAtIsNull(),
            )
            .fetchOne() ?: throw NoSuchElementException("회원이 존재하지 않습니다.")
    }

    override fun findByLoginId(loginId: String): Member {
        return queryFactory
            .selectFrom(member)
            .where(
                memberLoginIdEq(loginId = loginId),
                memberDeletedAtIsNull(),
            )
            .fetchOne() ?: throw NoSuchElementException("회원이 존재하지 않습니다.")
    }

    private fun memberIdEq(id: Long): BooleanExpression = member.id.eq(id)

    private fun memberLoginIdEq(loginId: String): BooleanExpression = member.loginId.eq(loginId)

    private fun memberDeletedAtIsNull(): BooleanExpression = member.deletedAt.isNull
}
