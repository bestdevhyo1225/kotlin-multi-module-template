package kr.co.hyo.domain.shop.entity

import jakarta.persistence.Column
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.envers.AuditTable
import org.hibernate.envers.Audited
import java.util.Objects

@Entity
@DynamicUpdate
@AuditTable(value = "shop_audit")
@Table(
    name = "shop",
    indexes = [],
)
class Shop private constructor(
    @EmbeddedId
    val shopId: ShopId,
    name: String,
) {

    @Audited
    @Column(nullable = false)
    var name: String = name
        protected set

    override fun hashCode(): Int = Objects.hash(shopId)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherShop: Shop = (other as? Shop) ?: return false
        // - '스레드 == 트랜잭션 == 영속성 컨텍스트' 범위내에서는 id 값만 같아도 같은 엔티티 객체로 본다.
        // - 영속성 컨텍스트는 REPEATABLE READ로 동작한다.
        return this.shopId == otherShop.shopId
    }

    override fun toString(): String = "Shop(serviceType=${shopId.serviceType}, id=${shopId.id}, name=$name)"

    companion object {
        operator fun invoke(shopId: ShopId, name: String): Shop = Shop(shopId = shopId, name = name)
    }

    fun changeName(name: String) {
        this.name = name
    }
}
