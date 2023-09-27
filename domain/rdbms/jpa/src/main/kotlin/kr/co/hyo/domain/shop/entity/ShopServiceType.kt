package kr.co.hyo.domain.shop.entity

enum class ShopServiceType {
    SERVICE_A,
    SERVICE_B;

    companion object {
        fun convert(value: String): ShopServiceType {
            return try {
                valueOf(value.uppercase())
            } catch (exception: Exception) {
                throw RuntimeException("유효하지 않음")
            }
        }
    }
}
