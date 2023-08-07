package kr.co.hyo.domain.common.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.Objects

@Entity
@Table(name = "dummy")
class Dummy private constructor(
    name: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(nullable = false, length = 30)
    var name: String = name
        protected set

    override fun hashCode(): Int = Objects.hash(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherDummy: Dummy = (other as? Dummy) ?: return false
        // - '스레드 == 트랜잭션 == 영속성 컨텍스트' 범위내에서는 id 값만 같아도 같은 엔티티 객체로 본다.
        // - 영속성 컨텍스트는 REPEATABLE READ로 동작한다.
        return this.id == otherDummy.id
    }

    override fun toString(): String = "Dummy(id=$id, name=$name)"

    companion object {
        operator fun invoke(name: String): Dummy = Dummy(name = name)
    }
}
