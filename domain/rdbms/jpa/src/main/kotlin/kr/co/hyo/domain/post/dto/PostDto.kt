package kr.co.hyo.domain.post.dto

import java.time.LocalDate
import java.time.LocalDateTime

data class PostDto(
    val id: Long,
    val memberId: Long,
    val title: String,
    val contents: String,
    val likeCount: Long,
    val viewCount: Long,
    val createdDate: LocalDate,
    val createdDatetime: LocalDateTime,
)
