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
    name: String,
    startDatetime: LocalDateTime,
    endDatetime: LocalDateTime,
) : BaseEntity() {

    @Column(nullable = false)
    var name: String = name
        protected set

    @Column(nullable = false, columnDefinition = "DATETIME")
    var startDatetime: LocalDateTime = startDatetime
        protected set

    @Column(nullable = false, columnDefinition = "DATETIME")
    var endDatetime: LocalDateTime = endDatetime
        protected set

    override fun toString(): String =
        "Reservation(id=$id, createdDate=$createdDate, createdDatetime=$createdDatetime, " +
            "startDatetime=$startDatetime, endDatetime=$endDatetime, updatedDate=$updatedDate, " +
            "updatedDatetime=$updatedDatetime, deletedDatetime=$deletedDatetime)"

    companion object {
        operator fun invoke(
            name: String,
            startDatetime: LocalDateTime,
            endDatetime: LocalDateTime,
        ) = Reservation(
            name = name,
            startDatetime = startDatetime,
            endDatetime = endDatetime,
        )
    }
}
