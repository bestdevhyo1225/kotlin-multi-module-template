package kr.co.hyo.domain.post.repository.querydsl

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.hyo.domain.post.entity.Post
import kr.co.hyo.domain.post.entity.QPost.post
import kr.co.hyo.domain.post.repository.PostJpaRepositorySupport
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class PostJpaQueryDslRepositorySupport(
    private val queryFactory: JPAQueryFactory,
) : PostJpaRepositorySupport {

    override fun findByMemberIdAndId(memberId: Long, id: Long): Post {
        return queryFactory
            .selectFrom(post)
            .where(
                postMemberIdEq(memberId = memberId),
                postIdEq(id = id),
                postDeletedDatetimeIsNull(),
            )
            .fetchOne() ?: throw NoSuchElementException("게시글이 존재하지 않습니다.")
    }

    override fun findAllByMemberIdAndId(memberId: Long, lastId: Long, limit: Long): List<Post> {
        return queryFactory
            .selectFrom(post)
            .where(
                postMemberIdEq(memberId = memberId),
                postIdGt(id = lastId),
                postDeletedDatetimeIsNull(),
            )
            .fetch()
    }

    private fun postIdEq(id: Long): BooleanExpression = post.id.eq(id)

    private fun postIdGt(id: Long): BooleanExpression = post.id.gt(id)

    private fun postMemberIdEq(memberId: Long): BooleanExpression = post.memberId.eq(memberId)

    private fun postDeletedDatetimeIsNull(): BooleanExpression = post.deletedDatetime.isNull
}
