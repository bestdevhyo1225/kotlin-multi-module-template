package kr.co.hyo.domain.shop.mapper

import kr.co.hyo.domain.shop.dto.ShopAuditDto
import kr.co.hyo.domain.shop.entity.ShopAudit
import kr.co.hyo.domain.shop.entity.v2.ShopAuditV2

object ShopAuditDtoMapper {

    fun toDto(shopAudit: ShopAudit): ShopAuditDto =
        with(receiver = shopAudit) {
            ShopAuditDto(
                shopServiceType = shopId.serviceType.name,
                shopId = shopId.id,
                rev = rev,
                name = name
            )
        }

    fun toDto(shopAuditV2: ShopAuditV2): ShopAuditDto =
        with(receiver = shopAuditV2) {
            ShopAuditDto(
                shopServiceType = serviceType.name,
                shopId = id,
                rev = rev,
                name = name
            )
        }
}
