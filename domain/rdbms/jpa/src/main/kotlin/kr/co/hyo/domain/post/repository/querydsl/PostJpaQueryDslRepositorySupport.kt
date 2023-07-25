package kr.co.hyo.domain.post.repository.querydsl

import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.hyo.domain.post.entity.Post
import kr.co.hyo.domain.post.entity.QPost.post
import kr.co.hyo.domain.post.repository.PostJpaRepositorySupport
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository
@Transactional(readOnly = true)
class PostJpaQueryDslRepositorySupport(
    private val queryFactory: JPAQueryFactory,
) : PostJpaRepositorySupport {

    override fun find(id: Long): Post {
        return queryFactory
            .selectFrom(post)
            .where(
                postIdEq(id = id),
                postDeletedDatetimeIsNull(),
            )
            .fetchOne() ?: throw NoSuchElementException("게시글이 존재하지 않습니다.")
    }

    override fun findAll(ids: List<Long>): List<Post> {
        if (ids.isEmpty()) {
            return emptyList()
        }
        return queryFactory
            .selectFrom(post)
            .where(
                postIdIn(ids = ids),
                postDeletedDatetimeIsNull(),
            )
            .orderBy(postIdDesc())
            .fetch()
    }

    override fun findIds(
        memberIds: List<Long>,
        timelineUpdatedDatetime: LocalDateTime?,
        lastId: Long,
        limit: Long,
    ): List<Long> {
        if (memberIds.isEmpty()) {
            return emptyList()
        }

        return queryFactory
            .select(post.id)
            .from(post)
            .where(
                postMemberIdIn(memberIds = memberIds),
                postDeletedDatetimeIsNull(),
                postCreatedDatetimeGoe(createdDatetime = timelineUpdatedDatetime),
                postIdGt(id = lastId),
            )
            .limit(limit)
            .orderBy(postIdAsc())
            .fetch()
    }

    private fun postIdEq(id: Long): BooleanExpression = post.id.eq(id)

    private fun postIdGt(id: Long): BooleanExpression = post.id.gt(id)

    private fun postIdIn(ids: List<Long>): BooleanExpression = post.id.`in`(ids)

    private fun postMemberIdIn(memberIds: List<Long>): BooleanExpression = post.memberId.`in`(memberIds)

    private fun postCreatedDatetimeGoe(createdDatetime: LocalDateTime?): BooleanExpression? =
        if (createdDatetime == null) null else post.createdDatetime.goe(createdDatetime)

    private fun postDeletedDatetimeIsNull(): BooleanExpression = post.deletedDatetime.isNull

    private fun postIdAsc(): OrderSpecifier<Long> = post.id.asc()

    private fun postIdDesc(): OrderSpecifier<Long> = post.id.desc()
}
