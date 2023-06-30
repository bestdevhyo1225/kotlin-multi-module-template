package kr.co.hyo.common.util.page

import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero

class PageByPosition<T>(
    val items: List<T>,
    val nextPageRequestByPosition: PageRequestByPosition,
)

data class PageRequestByPosition(
    @field:PositiveOrZero(message = "start는 0보다 같거나 큰 값을 입력하세요")
    val start: Long,

    @field:Positive(message = "size는 0보다 큰 값을 입력하세요")
    val size: Long,
) {

    fun next(itemSize: Int): PageRequestByPosition {
        val nextStart: Long = if (itemSize <= 0 || itemSize < size) -1L else start.plus(size)
        return PageRequestByPosition(start = nextStart, size = size)
    }
}
