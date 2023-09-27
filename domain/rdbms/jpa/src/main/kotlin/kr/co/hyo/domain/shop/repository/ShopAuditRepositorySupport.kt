package kr.co.hyo.domain.shop.repository

import kr.co.hyo.domain.shop.entity.ShopAudit
import kr.co.hyo.domain.shop.entity.ShopServiceType

interface ShopAuditRepositorySupport {
    fun findAllByShopIdAndLimit(id: Long, serviceType: ShopServiceType, limit: Long): List<ShopAudit>
}
