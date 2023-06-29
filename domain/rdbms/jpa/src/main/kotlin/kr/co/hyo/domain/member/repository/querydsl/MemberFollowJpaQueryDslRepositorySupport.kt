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

    override fun findAllByFollowingId(followingId: Long, lastId: Long, limit: Long): List<MemberFollow> {
        return queryFactory
            .selectFrom(memberFollow)
            .where(
                memberFollowFollowingIdEq(followingId = followingId),
                memberFollowIdGt(id = lastId),
            )
            .limit(limit)
            .fetch()
    }

    private fun memberFollowFollowingIdEq(followingId: Long): BooleanExpression =
        memberFollow.followingId.eq(followingId)

    private fun memberFollowIdGt(id: Long): BooleanExpression =
        memberFollow.id.gt(id)
}
