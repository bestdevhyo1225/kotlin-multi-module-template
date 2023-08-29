package kr.co.hyo.domain.reservation.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import kr.co.hyo.domain.common.entity.BaseEntity
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@DynamicUpdate
@Table(
    name = "reservation_count",
    indexes = [],
)
class ReservationCount private constructor(
    reservationId: Long,
    maxNumber: Int,
) : BaseEntity() {

    @Column(nullable = false)
    var reservationId: Long = reservationId
        protected set

    @Column(nullable = false)
    var maxNumber: Int = maxNumber
        protected set

    override fun toString(): String =
        "ReservationCount(id=$id, reservationId=$reservationId, maxNumber=$maxNumber, " +
            "createdDate=$createdDate, createdDatetime=$createdDatetime, " +
            "updatedDate=$updatedDate, updatedDatetime=$updatedDatetime, deletedDatetime=$deletedDatetime)"

    companion object {
        operator fun invoke(reservationId: Long, maxNumber: Int) =
            ReservationCount(reservationId = reservationId, maxNumber = maxNumber)
    }

    fun incrementMaxNumber() {
        this.maxNumber += 1
        this.updatedDate = LocalDate.now()
        this.updatedDatetime = LocalDateTime.now()
    }
}
