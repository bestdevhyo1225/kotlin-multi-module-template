package kr.co.hyo.domain.shop.entity

import jakarta.persistence.Column
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "shop_audit")
class ShopAudit(
    @EmbeddedId
    val shopId: ShopId,

    @Column(nullable = false)
    val rev: Long,

    @Column(nullable = false)
    val revtype: Int,

    @Column(nullable = false)
    val name: String,
) {

    override fun toString(): String =
        "ShopAudit(rev=$rev, revtype=$revtype, id=${shopId.id}, serviceType=${shopId.serviceType}, name=$name)"
}
