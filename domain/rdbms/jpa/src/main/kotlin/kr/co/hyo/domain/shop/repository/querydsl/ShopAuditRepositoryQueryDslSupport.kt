package kr.co.hyo.domain.shop.repository.querydsl

import com.querydsl.core.types.OrderSpecifier
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.hyo.domain.shop.entity.QShopAudit.shopAudit
import kr.co.hyo.domain.shop.entity.ShopAudit
import kr.co.hyo.domain.shop.entity.ShopServiceType
import kr.co.hyo.domain.shop.repository.ShopAuditRepositorySupport
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class ShopAuditRepositoryQueryDslSupport(
    private val queryFactory: JPAQueryFactory,
) : ShopAuditRepositorySupport {

    override fun findAllByShopIdAndLimit(id: Long, serviceType: ShopServiceType, limit: Long): List<ShopAudit> =
        queryFactory
            .selectFrom(shopAudit)
            .where(
                shopAuditIdEq(id = id),
                shopAuditServiceTypeEq(serviceType = serviceType),
            )
            .orderBy(shopAuditRevDesc())
            .limit(limit)
            .fetch()

    private fun shopAuditIdEq(id: Long) = shopAudit.shopId.id.eq(id)

    private fun shopAuditServiceTypeEq(serviceType: ShopServiceType) = shopAudit.shopId.serviceType.eq(serviceType)

    private fun shopAuditRevDesc(): OrderSpecifier<Long> = shopAudit.rev.desc()
}
