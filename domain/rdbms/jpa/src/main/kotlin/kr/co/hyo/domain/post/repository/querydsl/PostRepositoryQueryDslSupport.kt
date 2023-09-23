package kr.co.hyo.domain.post.repository.querydsl

import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions.numberTemplate
import com.querydsl.core.types.dsl.NumberTemplate
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.hyo.domain.post.entity.Post
import kr.co.hyo.domain.post.entity.QPost.post
import kr.co.hyo.domain.post.repository.PostRepositorySupport
import kr.co.hyo.domain.post.repository.querydsl.PostRepositoryQueryDslSupport.FunctionTempate.MATCH_AGAINST
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime

@Repository
@Transactional(readOnly = true)
class PostRepositoryQueryDslSupport(
    private val queryFactory: JPAQueryFactory,
) : PostRepositorySupport {

    object FunctionTempate {
        const val MATCH_AGAINST = "function('match_against', {0}, {1}, {2})"
    }

    override fun find(id: Long, memberId: Long): Post {
        return queryFactory
            .selectFrom(post)
            .where(
                postIdEq(id = id),
                postMemberIdEq(memberId = memberId),
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

    override fun findAll(keyword: String, offset: Long, limit: Long): List<Post> {
        return queryFactory
            .selectFrom(post)
            .where(
                postKeywordMatchAgainstGtZero(keyword = keyword),
                postDeletedDatetimeIsNull(),
            )
            .orderBy(postKeywordMatchAgainstDesc(keyword = keyword))
            .limit(limit)
            .offset(offset)
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
                postCreatedDatetimeGoe(createdDatetime = timelineUpdatedDatetime),
                postDeletedDatetimeIsNull(),
                postIdGt(id = lastId),
            )
            .orderBy(postIdAsc())
            .limit(limit)
            .fetch()
    }

    private fun postIdEq(id: Long): BooleanExpression = post.id.eq(id)

    private fun postIdGt(id: Long): BooleanExpression = post.id.gt(id)

    private fun postIdIn(ids: List<Long>): BooleanExpression = post.id.`in`(ids)

    private fun postKeywordMatchAgainstGtZero(keyword: String): BooleanExpression =
        postKeywordNumberTemplate(keyword = keyword).gt(0)

    private fun postMemberIdEq(memberId: Long): BooleanExpression = post.memberId.eq(memberId)

    private fun postMemberIdIn(memberIds: List<Long>): BooleanExpression = post.memberId.`in`(memberIds)

    private fun postCreatedDatetimeGoe(createdDatetime: LocalDateTime?): BooleanExpression? =
        if (createdDatetime == null) null else post.createdDatetime.goe(createdDatetime)

    private fun postDeletedDatetimeIsNull(): BooleanExpression = post.deletedDatetime.isNull

    private fun postIdAsc(): OrderSpecifier<Long> = post.id.asc()

    private fun postIdDesc(): OrderSpecifier<Long> = post.id.desc()

    private fun postKeywordMatchAgainstDesc(keyword: String): OrderSpecifier<BigDecimal> =
        postKeywordNumberTemplate(keyword = keyword).desc()

    private fun postKeywordNumberTemplate(keyword: String): NumberTemplate<BigDecimal> =
        numberTemplate(BigDecimal::class.java, MATCH_AGAINST, post.title, post.contents, "+${keyword}*")
}
