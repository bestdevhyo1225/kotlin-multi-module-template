package kr.co.hyo.domain.member.repository.querydsl

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.hyo.domain.member.entity.MemberFollow
import kr.co.hyo.domain.member.entity.QMemberFollow.memberFollow
import kr.co.hyo.domain.member.repository.MemberFollowJpaRepositorySupport
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class MemberFollowJpaQueryDslRepositorySupport(
    private val queryFactory: JPAQueryFactory,
) : MemberFollowJpaRepositorySupport {

    override fun findAllByMemberId(memberId: Long, lastFollowerId: Long, limit: Long): List<MemberFollow> {
        return queryFactory
            .selectFrom(memberFollow)
            .where(
                memberFollowMemberIdEq(memberId = memberId),
                memberFollowFollowerIdGt(followerId = lastFollowerId),
            )
            .limit(limit)
            .fetch()
    }

    private fun memberFollowMemberIdEq(memberId: Long): BooleanExpression = memberFollow.member.id.eq(memberId)

    private fun memberFollowFollowerIdGt(followerId: Long): BooleanExpression =
        memberFollow.followerId.gt(followerId)
}
