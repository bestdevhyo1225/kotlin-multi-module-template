package kr.co.hyo.domain.shop.repository.querydsl

import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.hyo.domain.shop.entity.ShopServiceType
import kr.co.hyo.domain.shop.entity.v2.QShopAuditV2.shopAuditV2
import kr.co.hyo.domain.shop.entity.v2.ShopAuditV2
import kr.co.hyo.domain.shop.repository.v2.ShopAuditV2RepositorySupport
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class ShopAuditV2RepositoryQueryDslSupport(
    private val queryFactory: JPAQueryFactory,
) : ShopAuditV2RepositorySupport {

    override fun findAllByShopIdAndLimit(id: Long, serviceType: ShopServiceType, limit: Long): List<ShopAuditV2> =
        queryFactory
            .selectFrom(shopAuditV2)
            .where(
                shopAuditV2IdEq(id = id),
                shopAuditV2ServiceTypeEq(serviceType = serviceType),
            )
            .orderBy(shopAuditV2RevDesc())
            .limit(limit)
            .fetch()

    private fun shopAuditV2IdEq(id: Long): BooleanExpression = shopAuditV2.id.eq(id)

    private fun shopAuditV2ServiceTypeEq(serviceType: ShopServiceType): BooleanExpression =
        shopAuditV2.serviceType.eq(serviceType)

    private fun shopAuditV2RevDesc(): OrderSpecifier<Long> = shopAuditV2.rev.desc()
}
