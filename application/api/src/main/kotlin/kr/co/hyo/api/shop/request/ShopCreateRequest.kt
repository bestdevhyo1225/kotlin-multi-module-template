package kr.co.hyo.api.shop.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import kr.co.hyo.domain.shop.dto.ShopCreateDto

data class ShopCreateRequest(
    @field:NotBlank(message = "서비스 타입을 입력하세요")
    @field:Schema(description = "서비스 타입", example = "SERVICE_A", required = true)
    val serviceType: String,

    @field:Positive(message = "고유 ID는 0보다 큰 값을 입력하세요")
    @field:Schema(description = "고유 ID", example = "1", required = true)
    val id: Long,

    @field:NotBlank(message = "매장 이름을 입력하세요")
    @field:Schema(description = "매장 이름", example = "테스트-1", required = true)
    val name: String,
) {

    fun toSeviceDto(): ShopCreateDto =
        ShopCreateDto(serviceType = serviceType, id = id, name = name)
}
