package kr.co.hyo.domain.shop.service.v1

import kr.co.hyo.domain.shop.dto.ShopChangeNameDto
import kr.co.hyo.domain.shop.dto.ShopCreateDto
import kr.co.hyo.domain.shop.entity.Shop
import kr.co.hyo.domain.shop.entity.ShopId
import kr.co.hyo.domain.shop.entity.ShopServiceType
import kr.co.hyo.domain.shop.entity.v2.ShopV2
import kr.co.hyo.domain.shop.repository.ShopRepository
import kr.co.hyo.domain.shop.repository.v2.ShopV2Repository
import kr.co.hyo.domain.shop.service.ShopWriteService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ShopWriteServiceV1(
    private val shopRepository: ShopRepository,
    private val shopV2Repository: ShopV2Repository,
) : ShopWriteService {

    override fun create(dto: ShopCreateDto) {
        val (serviceType: String, id: Long, name: String) = dto
        val shopId = ShopId(serviceType = ShopServiceType.convert(value = serviceType), id = id)
        val shop = Shop(shopId = shopId, name = name)
        shopRepository.save(shop)

        val shop2 = ShopV2(serviceType = serviceType, name = name)
        shopV2Repository.save(shop2)
    }

    override fun updateName(dto: ShopChangeNameDto) {
        val (serviceType: String, id: Long, name: String) = dto
        val shopId = ShopId(serviceType = ShopServiceType.convert(value = serviceType), id = id)
        val shop: Shop = shopRepository.findByIdOrNull(id = shopId)
            ?: throw NoSuchElementException("매장이 존재하지 않습니다.")
        shop.changeName(name = name)

        val shop2: ShopV2 = shopV2Repository.findByIdOrNull(id = id)
            ?: throw NoSuchElementException("매장이 존재하지 않습니다.")
        shop2.changeName(name = name)
    }
}
