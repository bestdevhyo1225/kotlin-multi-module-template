package kr.co.hyo.domain.common.repository.querydsl

import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.hyo.domain.common.entity.QDummy.dummy
import kr.co.hyo.domain.common.repository.DummyRepositorySupport
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class DummyRepositoryQueryDslSupport(
    private val queryFactory: JPAQueryFactory,
) : DummyRepositorySupport {

    override fun findMinId(): Long =
        queryFactory
            .select(dummy.id.min())
            .from(dummy)
            .fetchOne() ?: 0L

    override fun findMaxId(): Long =
        queryFactory
            .select(dummy.id.max())
            .from(dummy)
            .fetchOne() ?: Long.MAX_VALUE
}
