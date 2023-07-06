package kr.co.hyo.domain.reservation.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import kr.co.hyo.domain.common.entity.BaseEntity
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime

@Entity
@DynamicUpdate
@Table(
    name = "reservation",
    indexes = [],
)
class Reservation private constructor(
    type: String,
    startDatetime: LocalDateTime,
    endDatetime: LocalDateTime,
) : BaseEntity() {

    @Column(nullable = false)
    var type: String = type
        protected set

    @Column(nullable = false, columnDefinition = "DATETIME")
    var startDatetime: LocalDateTime = startDatetime
        protected set

    @Column(nullable = false, columnDefinition = "DATETIME")
    var endDatetime: LocalDateTime = endDatetime
        protected set

    override fun toString(): String =
        "Reservation(id=$id, type=$type, createdDate=$createdDate, createdDatetime=$createdDatetime, " +
            "startDatetime=$startDatetime, endDatetime=$endDatetime, updatedDate=$updatedDate, " +
            "updatedDatetime=$updatedDatetime, deletedDatetime=$deletedDatetime)"

    companion object {
        operator fun invoke(
            type: String,
            startDatetime: LocalDateTime,
            endDatetime: LocalDateTime,
        ) = Reservation(
            type = type,
            startDatetime = startDatetime,
            endDatetime = endDatetime,
        )
    }
}
