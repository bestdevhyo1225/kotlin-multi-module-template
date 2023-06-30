package kr.co.hyo.domain.post.dto

import java.time.LocalDate
import java.time.LocalDateTime

data class PostCacheDto(
    val postId: Long,
    val memberId: Long,
    val title: String,
    val contents: String,
    val createdDate: LocalDate,
    val createdDatetime: LocalDateTime,
)
