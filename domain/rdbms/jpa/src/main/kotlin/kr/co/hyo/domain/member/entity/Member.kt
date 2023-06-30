package kr.co.hyo.domain.member.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Index
import jakarta.persistence.Table
import kr.co.hyo.domain.common.entity.BaseEntity
import org.hibernate.annotations.DynamicUpdate
import org.mindrot.jbcrypt.BCrypt
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@DynamicUpdate
@Table(
    name = "member",
    indexes = [Index(name = "uidx_member_01", columnList = "loginId", unique = true)]
)
class Member private constructor(
    name: String,
    loginId: String,
    password: String,
    email: String,
    followCount: Long,
    followingCount: Long,
) : BaseEntity() {

    @Column(nullable = false)
    var name: String = name
        protected set

    @Column(nullable = false)
    var loginId: String = loginId
        protected set

    @Column(nullable = false)
    var password: String = password
        protected set

    @Column(nullable = false)
    var email: String = email
        protected set

    @Column(nullable = false)
    var followCount: Long = followCount
        protected set

    @Column(nullable = false)
    var followingCount: Long = followingCount
        protected set

    @Column(columnDefinition = "DATETIME")
    var timelineUpdatedDatetime: LocalDateTime? = null
        protected set

    override fun toString(): String =
        "Member(id=$id, name=$name, loginId=$loginId, password=$password, email=$email, followCount=$followCount, " +
            "followingCount=$followingCount, timelineUpdatedDatetime=$timelineUpdatedDatetime, " +
            "createdDate=$createdDate, createdDatetime=$createdDatetime, updatedDate=$updatedDate, " +
            "updatedDatetime=$updatedDatetime, deletedDatetime=$deletedDatetime)"

    companion object {
        private const val MEMBER_ID = "memberId"
        private const val MEMBER_EMAIL = "memberEmail"

        const val MEMBER_FANOUT_MAX_LIMIT = 1_000_000L

        operator fun invoke(name: String, loginId: String, password: String, email: String) =
            Member(
                name = name,
                loginId = loginId,
                password = BCrypt.hashpw(password, BCrypt.gensalt()),
                email = email,
                followCount = 0L,
                followingCount = 0L,
            )
    }

    fun changePassword(oldPassword: String, newPassword: String) {
        verifyPassword(password = oldPassword)

        val encryptedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt())
        if (encryptedNewPassword == this.password) {
            return
        }

        this.password = encryptedNewPassword
        this.updatedDate = LocalDate.now()
        this.updatedDatetime = LocalDateTime.now()
    }

    fun verifyPassword(password: String) {
        if (!BCrypt.checkpw(password, this.password)) {
            throw IllegalArgumentException("비밀번호가 일치하지 않습니다.")
        }
    }

    fun changeEmail(email: String) {
        if (this.email == email) {
            return
        }

        this.email = email
        this.updatedDate = LocalDate.now()
        this.updatedDatetime = LocalDateTime.now()
    }

    fun getJwtClaims(): Map<String, Any> = mapOf(MEMBER_ID to this.id!!, MEMBER_EMAIL to this.email)

    fun incrementFollowCount() {
        this.followCount += 1
    }

    fun incrementFollowingCount() {
        this.followingCount += 1
    }

    fun changeTimelineUpdatedDatetime() {
        this.timelineUpdatedDatetime = LocalDateTime.now()
    }
}
