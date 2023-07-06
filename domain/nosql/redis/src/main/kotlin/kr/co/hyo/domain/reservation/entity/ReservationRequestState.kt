package kr.co.hyo.domain.reservation.entity

enum class ReservationRequestState(val label: String) {
    FAILED(label = "실패"),
    EXIT(label = "종료"),
    COMPLETED(label = "완료"),
    READY(label = "준비");

    companion object {
        fun convert(result: Long): ReservationRequestState {
            return when (result) {
                -2L -> FAILED
                -1L -> EXIT
                0L -> COMPLETED
                1L -> READY
                else -> throw RuntimeException("존재하지 않는 결과입니다.")
            }
        }
    }
}
