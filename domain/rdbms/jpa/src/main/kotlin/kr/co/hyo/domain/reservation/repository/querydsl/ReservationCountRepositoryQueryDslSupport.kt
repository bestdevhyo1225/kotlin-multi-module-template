package kr.co.hyo.domain.reservation.repository.querydsl

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.LockModeType.PESSIMISTIC_WRITE
import kr.co.hyo.domain.reservation.entity.QReservationCount.reservationCount
import kr.co.hyo.domain.reservation.entity.ReservationCount
import kr.co.hyo.domain.reservation.repository.ReservationCountRepositorySupport
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class ReservationCountRepositoryQueryDslSupport(
    private val queryFactory: JPAQueryFactory,
) : ReservationCountRepositorySupport {

    override fun findByReservationIdAndPessimisticWriteLock(reservationId: Long): ReservationCount? =
        queryFactory
            .selectFrom(reservationCount)
            .where(reservationCountReservationIdEq(reservationId = reservationId))
            .setLockMode(PESSIMISTIC_WRITE)
            .fetchOne()

    private fun reservationCountReservationIdEq(reservationId: Long): BooleanExpression =
        reservationCount.reservationId.eq(reservationId)
}
