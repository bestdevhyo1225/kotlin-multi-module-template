package kr.co.hyo.common.util.page

data class SimplePage<T>(
    val items: List<T>,
    val page: Long,
    val size: Long,
    val total: Long,
)
