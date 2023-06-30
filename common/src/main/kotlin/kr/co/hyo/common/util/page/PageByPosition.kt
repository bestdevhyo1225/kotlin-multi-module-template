package kr.co.hyo.common.util.page

import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero

class PageByPosition<T>(
    val items: List<T>,
    val nextPageRequestByPosition: PageRequestByPosition,
)

data class PageRequestByPosition(
    @field:PositiveOrZero(message = "start는 0보다 같거나 큰 값을 입력하세요")
    val start: Long = 0,

    @field:Positive(message = "size는 0보다 큰 값을 입력하세요")
    val size: Long = 10,
) {

    companion object {
        const val NONE_START = -1L
    }

    fun next(itemSize: Int): PageRequestByPosition {
        if (itemSize < 0) {
            throw IllegalArgumentException("itemSize is negative value")
        }
        val nextStart: Long = if (itemSize == 0 || itemSize < size) NONE_START else start.plus(size)
        return PageRequestByPosition(start = nextStart, size = size)
    }
}
