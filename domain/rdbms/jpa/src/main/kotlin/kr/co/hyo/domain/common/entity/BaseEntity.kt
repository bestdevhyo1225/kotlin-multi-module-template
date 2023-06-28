package kr.co.hyo.domain.common.entity

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import java.time.LocalDateTime
import java.util.Objects

@MappedSuperclass
abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long? = null

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME")
    val createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME")
    var updatedAt: LocalDateTime = LocalDateTime.now()
        protected set

    @Column(name = "deleted_at", columnDefinition = "DATETIME")
    var deletedAt: LocalDateTime? = null
        protected set

    override fun hashCode(): Int = Objects.hash(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherBase: BaseEntity = (other as? BaseEntity) ?: return false
        // - '스레드 == 트랜잭션 == 영속성 컨텍스트' 범위내에서는 id 값만 같아도 같은 엔티티 객체로 본다.
        // - 영속성 컨텍스트는 REPEATABLE READ로 동작한다.
        return this.id == otherBase.id
    }
}
