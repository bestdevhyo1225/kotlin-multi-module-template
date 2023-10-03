package kr.co.hyo.domain.shop.mapper

import kr.co.hyo.domain.shop.dto.ShopAuditDto
import kr.co.hyo.domain.shop.entity.ShopAudit

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
}
