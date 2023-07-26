package kr.co.hyo.domain.post.repository.querydsl

import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions.numberTemplate
import com.querydsl.core.types.dsl.NumberTemplate
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.hyo.domain.post.entity.Post
import kr.co.hyo.domain.post.entity.QPost.post
import kr.co.hyo.domain.post.repository.PostRepositorySupport
import kr.co.hyo.domain.post.repository.querydsl.PostRepositoryQueryDslSupport.FunctionTempate.MATCH_AGAINST
import kr.co.hyo.domain.post.repository.querydsl.PostRepositoryQueryDslSupport.SearchType.CONTENTS
import kr.co.hyo.domain.post.repository.querydsl.PostRepositoryQueryDslSupport.SearchType.TITLE
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime

@Repository
@Transactional(readOnly = true)
class PostRepositoryQueryDslSupport(
    private val queryFactory: JPAQueryFactory,
) : PostRepositorySupport {

    object SearchType {
        const val TITLE = "TITLE"
        const val CONTENTS = "CONTENTS"
    }

    object FunctionTempate {
        const val MATCH_AGAINST = "function('match_against', {0}, {1})"
    }

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

    override fun findAll(type: String, keyword: String, offset: Long, limit: Long): List<Post> {
        val column: StringPath = getMatchColumn(type = type)
        return queryFactory
            .selectFrom(post)
            .where(
                postKeywordMatchAgainstGtZero(column = column, keyword = keyword),
                postDeletedDatetimeIsNull(),
            )
            .orderBy(postKeywordMatchAgainstDesc(column = column, keyword = keyword))
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
                postDeletedDatetimeIsNull(),
                postCreatedDatetimeGoe(createdDatetime = timelineUpdatedDatetime),
                postIdGt(id = lastId),
            )
            .orderBy(postIdAsc())
            .limit(limit)
            .fetch()
    }

    private fun postIdEq(id: Long): BooleanExpression = post.id.eq(id)

    private fun postIdGt(id: Long): BooleanExpression = post.id.gt(id)

    private fun postIdIn(ids: List<Long>): BooleanExpression = post.id.`in`(ids)
    private fun postKeywordMatchAgainstGtZero(column: StringPath, keyword: String): BooleanExpression =
        postKeywordNumberTemplate(column = column, keyword = keyword).gt(0)

    private fun postMemberIdIn(memberIds: List<Long>): BooleanExpression = post.memberId.`in`(memberIds)

    private fun postCreatedDatetimeGoe(createdDatetime: LocalDateTime?): BooleanExpression? =
        if (createdDatetime == null) null else post.createdDatetime.goe(createdDatetime)

    private fun postDeletedDatetimeIsNull(): BooleanExpression = post.deletedDatetime.isNull

    private fun postIdAsc(): OrderSpecifier<Long> = post.id.asc()

    private fun postIdDesc(): OrderSpecifier<Long> = post.id.desc()

    private fun postKeywordMatchAgainstDesc(column: StringPath, keyword: String): OrderSpecifier<BigDecimal> =
        postKeywordNumberTemplate(column = column, keyword = keyword).desc()

    private fun postKeywordNumberTemplate(column: StringPath, keyword: String): NumberTemplate<BigDecimal> =
        numberTemplate(BigDecimal::class.java, MATCH_AGAINST, column, "+${keyword}*")

    private fun getMatchColumn(type: String): StringPath =
        when (type.uppercase()) {
            TITLE -> post.title
            CONTENTS -> post.contents
            else -> throw IllegalArgumentException("검색 타입이 존재하지 않습니다.")
        }
}
