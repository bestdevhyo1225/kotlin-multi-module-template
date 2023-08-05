package kr.co.hyo.domain.member.repository.querydsl

import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.hyo.domain.member.dto.MemberFollowDto
import kr.co.hyo.domain.member.entity.MemberFollow
import kr.co.hyo.domain.member.entity.QMember.member
import kr.co.hyo.domain.member.entity.QMemberFollow.memberFollow
import kr.co.hyo.domain.member.repository.MemberFollowRepositorySupport
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class MemberFollowRepositoryQueryDslSupport(
    private val queryFactory: JPAQueryFactory,
) : MemberFollowRepositorySupport {

    override fun findAll(followingId: Long, lastFollowerId: Long, limit: Long): List<MemberFollow> {
        return queryFactory
            .selectFrom(memberFollow)
            .where(
                memberFollowFollowingIdEq(followingId = followingId),
                memberFollowFollowerIdGt(followerId = lastFollowerId),
            )
            .orderBy(memberFollowFollowerIdAsc())
            .limit(limit)
            .fetch()
    }

    override fun findAllWithInnerJoin(followerId: Long, followCount: Long, limit: Long): List<MemberFollowDto> {
        return queryFactory
            .select(
                Projections.constructor(
                    MemberFollowDto::class.java,
                    memberFollow.id,
                    memberFollow.followingId,
                    memberFollow.followerId,
                    memberFollow.createdDate,
                )
            )
            .from(memberFollow)
            .innerJoin(member).on(memberIdEq(memberId = memberFollow.followingId))
            .where(
                memberFollowFollowerIdEq(followerId = followerId),
                memberFollowCountGt(followCount = followCount),
            )
            .limit(limit)
            .fetch()
    }

    private fun memberFollowFollowingIdEq(followingId: Long): BooleanExpression =
        memberFollow.followingId.eq(followingId)

    private fun memberFollowFollowerIdEq(followerId: Long): BooleanExpression =
        memberFollow.followerId.eq(followerId)

    private fun memberFollowFollowerIdGt(followerId: Long): BooleanExpression =
        memberFollow.followerId.gt(followerId)

    private fun memberIdEq(memberId: NumberPath<Long>): BooleanExpression = member.id.eq(memberId)

    private fun memberFollowCountGt(followCount: Long): BooleanExpression = member.followCount.gt(followCount)

    private fun memberFollowFollowerIdAsc(): OrderSpecifier<Long> = memberFollow.followerId.asc()
}
