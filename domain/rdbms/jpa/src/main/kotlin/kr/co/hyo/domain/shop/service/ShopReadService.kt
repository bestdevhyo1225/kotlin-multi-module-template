package kr.co.hyo.domain.shop.service

import kr.co.hyo.domain.shop.dto.ShopAuditDto

interface ShopReadService {
    fun findShopAudits(id: Long, serviceType: String, limit: Long): List<ShopAuditDto>
}
