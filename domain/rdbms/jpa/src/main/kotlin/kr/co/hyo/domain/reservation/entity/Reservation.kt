package kr.co.hyo.domain.reservation.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import kr.co.hyo.domain.common.entity.BaseEntity
import org.hibernate.annotations.DynamicUpdate

@Entity
@DynamicUpdate
@Table(
    name = "reservation",
    indexes = [],
)
class Reservation private constructor(
    type: String,
    memberId: Long,
) : BaseEntity() {

    @Column(nullable = false)
    var type: String = type
        protected set

    @Column(nullable = false)
    var memberId: Long = memberId
        protected set

    override fun toString(): String =
        "Reservation(id=$id, type=$type, memberId=$memberId, createdDate=$createdDate, createdDatetime=$createdDatetime, " +
            "updatedDate=$updatedDate, updatedDatetime=$updatedDatetime, deletedDatetime=$deletedDatetime)"

    companion object {
        operator fun invoke(type: String, memberId: Long) = Reservation(type = type, memberId = memberId)
    }
}
