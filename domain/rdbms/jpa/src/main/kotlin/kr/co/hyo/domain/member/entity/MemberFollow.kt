package kr.co.hyo.domain.member.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Objects

@Entity
@DynamicUpdate
@Table(
    name = "member_follow",
    indexes = [
        Index(name = "uidx_member_follow_01", columnList = "followingId,followerId", unique = true),
        Index(name = "idx_member_follow_02", columnList = "followingId,id"),
    ],
)
class MemberFollow private constructor(
    followingId: Long,
    followerId: Long,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(nullable = false)
    var followingId: Long = followingId
        protected set

    @Column(nullable = false)
    var followerId: Long = followerId
        protected set

    @Column(name = "created_date", nullable = false, columnDefinition = "DATE")
    val createdDate: LocalDate = LocalDate.now()

    @Column(name = "created_datetime", nullable = false, columnDefinition = "DATETIME")
    val createdDatetime: LocalDateTime = LocalDateTime.now()

    override fun hashCode(): Int = Objects.hash(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherMemberFollow: MemberFollow = (other as? MemberFollow) ?: return false
        // - '스레드 == 트랜잭션 == 영속성 컨텍스트' 범위내에서는 id 값만 같아도 같은 엔티티 객체로 본다.
        // - 영속성 컨텍스트는 REPEATABLE READ로 동작한다.
        return this.id == otherMemberFollow.id
    }

    override fun toString(): String =
        "MemberFollow(id=$id, followingId=$followingId, followerId=$followerId, createdDate=$createdDate, " +
            "createdDatetime=$createdDatetime)"

    companion object {
        operator fun invoke(followingId: Long, followerId: Long): MemberFollow {
            if (followingId == followerId) {
                throw IllegalArgumentException("자기 자신을 팔로우 할 수 없습니다.")
            }
            return MemberFollow(followingId = followingId, followerId = followerId)
        }
    }
}
