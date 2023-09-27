package kr.co.hyo.domain.shop.entity.v2

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.Table
import kr.co.hyo.domain.shop.entity.ShopServiceType
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.envers.AuditTable
import org.hibernate.envers.Audited
import java.util.Objects

@Entity
@DynamicUpdate
@AuditTable(value = "shop_audit_v2")
@Table(
    name = "shop_v2",
    indexes = [],
)
class ShopV2 private constructor(
    serviceType: ShopServiceType,
    name: String,
) {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long? = null

    @Audited
    @Column(name = "service_type", nullable = false)
    @Enumerated(STRING)
    var serviceType: ShopServiceType = serviceType
        protected set

    @Audited
    @Column(nullable = false)
    var name: String = name
        protected set

    override fun hashCode(): Int = Objects.hash(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherShopV2: ShopV2 = (other as? ShopV2) ?: return false
        // - '스레드 == 트랜잭션 == 영속성 컨텍스트' 범위내에서는 id 값만 같아도 같은 엔티티 객체로 본다.
        // - 영속성 컨텍스트는 REPEATABLE READ로 동작한다.
        return this.id == otherShopV2.id
    }

    override fun toString(): String = "ShopV2(id=${id}, serviceType=${serviceType}, name=$name)"

    companion object {
        operator fun invoke(serviceType: String, name: String): ShopV2 =
            ShopV2(serviceType = ShopServiceType.convert(value = serviceType), name = name)
    }

    fun changeName(name: String) {
        this.name = name
    }
}
