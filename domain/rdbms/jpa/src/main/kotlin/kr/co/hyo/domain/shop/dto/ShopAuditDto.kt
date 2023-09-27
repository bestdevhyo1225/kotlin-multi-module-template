package kr.co.hyo.domain.shop.dto

data class ShopAuditDto(
    val shopServiceType: String,
    val shopId: Long,
    val rev: Long,
    val name: String,
)
