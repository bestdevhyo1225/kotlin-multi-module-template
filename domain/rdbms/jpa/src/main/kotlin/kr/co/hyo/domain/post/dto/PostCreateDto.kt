package kr.co.hyo.domain.post.dto

data class PostCreateDto(
    val memberId: Long,
    val title: String,
    val contents: String,
)
