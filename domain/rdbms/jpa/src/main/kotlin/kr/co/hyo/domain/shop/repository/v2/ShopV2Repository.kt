package kr.co.hyo.domain.shop.repository.v2

import kr.co.hyo.domain.shop.entity.v2.ShopV2
import org.springframework.data.jpa.repository.JpaRepository

interface ShopV2Repository : JpaRepository<ShopV2, Long>
