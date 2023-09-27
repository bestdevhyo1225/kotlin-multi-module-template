package kr.co.hyo.domain.common.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.envers.RevisionEntity
import org.hibernate.envers.RevisionNumber
import org.hibernate.envers.RevisionTimestamp
import java.time.LocalDateTime

@Entity
@RevisionEntity
@Table(name = "audit_revision")
class AuditRevision {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @RevisionNumber
    val rev: Long? = null

    @RevisionTimestamp
    val createdDatetime: LocalDateTime? = null
}
