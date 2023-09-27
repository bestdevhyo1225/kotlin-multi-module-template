package kr.co.hyo.domain.shop.repository

import kr.co.hyo.domain.shop.entity.Shop
import kr.co.hyo.domain.shop.entity.ShopId
import org.springframework.data.jpa.repository.JpaRepository

interface ShopRepository : JpaRepository<Shop, ShopId>
