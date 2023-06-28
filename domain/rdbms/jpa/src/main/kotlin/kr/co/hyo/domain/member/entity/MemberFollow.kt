package kr.co.hyo.domain.member.entity

import jakarta.persistence.Column
import jakarta.persistence.ConstraintMode.NO_CONSTRAINT
import jakarta.persistence.Entity
import jakarta.persistence.FetchType.LAZY
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Objects

@Entity
@DynamicUpdate
@Table(
    name = "member_follow",
    indexes = [Index(name = "uidx_member_follow_01", columnList = "member_id,follower_id", unique = true)]
)
class MemberFollow private constructor(
    member: Member,
    followerId: Long,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", foreignKey = ForeignKey(value = NO_CONSTRAINT), nullable = false)
    var member: Member = member
        protected set

    @Column(name = "follower_id", nullable = false)
    var followerId: Long = followerId
        protected set

    @Column(name = "created_date", nullable = false, columnDefinition = "DATE")
    val createdDate: LocalDate = LocalDate.now()

    @Column(name = "created_datetime", nullable = false, columnDefinition = "DATETIME")
    val createdDateTime: LocalDateTime = LocalDateTime.now()

    override fun hashCode(): Int = Objects.hash(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherMemberFollow: MemberFollow = (other as? MemberFollow) ?: return false
        // - '스레드 == 트랜잭션 == 영속성 컨텍스트' 범위내에서는 id 값만 같아도 같은 엔티티 객체로 본다.
        // - 영속성 컨텍스트는 REPEATABLE READ로 동작한다.
        return this.id == otherMemberFollow.id
    }

    override fun toString(): String =
        "MemberFollow(id=$id, memberId=${member.id}, followerId=$followerId, createdDate=$createdDate, " +
            "createdDateTime=$createdDateTime)"

    companion object {
        operator fun invoke(member: Member, followerId: Long): MemberFollow {
            if (member.id == null) {
                throw IllegalArgumentException("팔로우 할 회원번호가 null 입니다.")
            }
            if (member.id == followerId) {
                throw IllegalArgumentException("자기 자신을 팔로우 할 수 없습니다.")
            }
            return MemberFollow(member = member, followerId = followerId)
        }
    }
}
