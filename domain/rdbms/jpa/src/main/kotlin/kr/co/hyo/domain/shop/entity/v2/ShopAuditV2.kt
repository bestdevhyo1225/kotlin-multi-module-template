package kr.co.hyo.domain.shop.entity.v2

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import kr.co.hyo.domain.shop.entity.ShopServiceType

@Entity
@Table(name = "shop_audit_v2")
class ShopAuditV2(
    @Id
    @Column(nullable = false)
    val rev: Long,

    @Column(nullable = false)
    val revtype: Int,

    @Column(nullable = false)
    val id: Long,

    @Column(name = "service_type", nullable = false)
    @Enumerated(STRING)
    val serviceType: ShopServiceType,

    @Column(nullable = false)
    val name: String,
) {

    override fun toString(): String =
        "ShopAuditV2(rev=$rev, revtype=$revtype, id=${id}, shopServiceType=${serviceType}, name=$name)"
}
