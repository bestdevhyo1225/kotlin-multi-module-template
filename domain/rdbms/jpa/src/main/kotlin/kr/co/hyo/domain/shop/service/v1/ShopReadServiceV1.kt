package kr.co.hyo.domain.shop.service.v1

import kr.co.hyo.domain.shop.dto.ShopAuditDto
import kr.co.hyo.domain.shop.entity.ShopAudit
import kr.co.hyo.domain.shop.entity.ShopId
import kr.co.hyo.domain.shop.entity.ShopServiceType
import kr.co.hyo.domain.shop.entity.v2.ShopAuditV2
import kr.co.hyo.domain.shop.mapper.ShopAuditDtoMapper
import kr.co.hyo.domain.shop.repository.ShopAuditRepositorySupport
import kr.co.hyo.domain.shop.repository.v2.ShopAuditV2RepositorySupport
import kr.co.hyo.domain.shop.service.ShopReadService
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ShopReadServiceV1(
    private val shopAuditRepositorySupport: ShopAuditRepositorySupport,
    private val shopAuditV2RepositorySupport: ShopAuditV2RepositorySupport,
) : ShopReadService {

    private val logger = KotlinLogging.logger {}

    override fun findShopAudits(id: Long, serviceType: String, limit: Long): List<ShopAuditDto> {
        val shopServiceType = ShopServiceType.convert(value = serviceType)
        val shopAudits: List<ShopAudit> =
            shopAuditRepositorySupport.findAllByShopIdAndLimit(id = id, serviceType = shopServiceType, limit = 5)
        val shopAuditV2s: List<ShopAuditV2> =
            shopAuditV2RepositorySupport.findAllByShopIdAndLimit(id = id, serviceType = shopServiceType, limit = 5)
        shopAuditV2s.forEach {
            logger.info { "shopAuditV2: $it" }
        }
        return shopAudits.map {
            logger.info { "shopAudit: $it" }
            ShopAuditDtoMapper.toDto(shopAudit = it)
        }
    }
}
