package kr.co.hyo.domain.shop.service

import kr.co.hyo.domain.shop.dto.ShopChangeNameDto
import kr.co.hyo.domain.shop.dto.ShopCreateDto

interface ShopWriteService {
    fun create(dto: ShopCreateDto)
    fun updateName(dto: ShopChangeNameDto)
}
