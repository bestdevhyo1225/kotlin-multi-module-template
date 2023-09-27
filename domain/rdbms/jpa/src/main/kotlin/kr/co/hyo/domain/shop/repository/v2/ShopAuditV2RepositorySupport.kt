package kr.co.hyo.domain.shop.repository.v2

import kr.co.hyo.domain.shop.entity.ShopServiceType
import kr.co.hyo.domain.shop.entity.v2.ShopAuditV2

interface ShopAuditV2RepositorySupport {
    fun findAllByShopIdAndLimit(id: Long, serviceType: ShopServiceType, limit: Long): List<ShopAuditV2>
}
