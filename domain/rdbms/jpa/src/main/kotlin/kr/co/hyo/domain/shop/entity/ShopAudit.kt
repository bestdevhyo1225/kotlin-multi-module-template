package kr.co.hyo.domain.shop.entity

import jakarta.persistence.Column
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "shop_audit")
class ShopAudit(
    @Id
    @Column(nullable = false)
    val rev: Long,

    @Column(nullable = false)
    val revtype: Int,

//    @EmbeddedId
//    val shopId: ShopId,

    @Column(nullable = false)
    val id: Long,

    @Column(name = "service_type", nullable = false)
    @Enumerated(STRING)
    val serviceType: ShopServiceType,

    @Column(nullable = false)
    val name: String,
) {

    override fun toString(): String =
        "ShopAudit(rev=$rev, revtype=$revtype, id=${id}, serviceType=${serviceType}, name=$name)"
}
