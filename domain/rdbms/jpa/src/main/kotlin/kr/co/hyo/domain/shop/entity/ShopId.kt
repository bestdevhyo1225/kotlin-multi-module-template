package kr.co.hyo.domain.shop.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import java.io.Serializable

@Embeddable
data class  ShopId(
    @Column(name = "service_type", nullable = false)
    @Enumerated(STRING)
    val serviceType: ShopServiceType,

    @Column(nullable = false)
    val id: Long,
) : Serializable
