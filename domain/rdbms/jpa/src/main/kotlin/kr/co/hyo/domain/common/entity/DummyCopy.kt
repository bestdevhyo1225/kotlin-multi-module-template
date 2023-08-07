package kr.co.hyo.domain.common.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.Objects

@Entity
@Table(name = "dummy_copy")
class DummyCopy private constructor(
    dummyId: Long,
    name: String,
) {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    var id: Long? = null

    @Column(nullable = false)
    var dummyId: Long = dummyId
        protected set

    @Column(nullable = false, length = 30)
    var name: String = name
        protected set

    override fun hashCode(): Int = Objects.hash(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherDummyCopy: DummyCopy = (other as? DummyCopy) ?: return false
        // - '스레드 == 트랜잭션 == 영속성 컨텍스트' 범위내에서는 id 값만 같아도 같은 엔티티 객체로 본다.
        // - 영속성 컨텍스트는 REPEATABLE READ로 동작한다.
        return this.id == otherDummyCopy.id
    }

    override fun toString(): String = "DummyCopy(id=$id, dummyId=$dummyId, name=$name)"

    companion object {
        operator fun invoke(dummyId: Long, name: String): DummyCopy = DummyCopy(dummyId = dummyId, name = name)
    }
}
