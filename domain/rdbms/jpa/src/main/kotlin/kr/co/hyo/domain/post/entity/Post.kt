package kr.co.hyo.domain.post.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import kr.co.hyo.domain.common.entity.BaseEntity
import org.hibernate.annotations.DynamicUpdate

@Entity
@DynamicUpdate
@Table(
    name = "post",
    indexes = [],
)
class Post private constructor(
    memberId: Long,
    title: String,
    contents: String,
) : BaseEntity() {

    @Column(nullable = false)
    var memberId: Long = memberId
        protected set

    @Column(nullable = false)
    var title: String = title
        protected set

    @Column(nullable = false)
    var contents: String = contents
        protected set

    @Column(nullable = false)
    var likeCount: Long = 0L
        protected set

    @Column(nullable = false)
    var viewCount: Long = 0L
        protected set

    override fun toString(): String =
        "POST(id=$id, memberId=$memberId, title=$title, contents=$contents, likeCount=$likeCount, " +
            "viewCount=$viewCount, createdDate=$createdDate, createdDatetime=$createdDatetime, " +
            "updatedDate=$updatedDate, updatedDatetime=$updatedDatetime, deletedDatetime=$deletedDatetime)"

    companion object {
        operator fun invoke(memberId: Long, title: String, contents: String): Post =
            Post(
                memberId = memberId,
                title = title,
                contents = contents,
            )
    }

    fun decrementLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount -= 1
        }
    }

    fun incrementLikeCount(memberId: Long) {
        if (this.memberId == memberId) {
            return
        }
        this.likeCount += 1
    }

    fun incrementViewCount(memberId: Long) {
        if (this.memberId == memberId) {
            return
        }
        this.viewCount += 1
    }
}
