package kr.co.hyo.api.shop.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kr.co.hyo.api.shop.request.ShopChangeNameRequest
import kr.co.hyo.api.shop.request.ShopCreateRequest
import kr.co.hyo.domain.shop.dto.ShopAuditDto
import kr.co.hyo.domain.shop.service.ShopReadService
import kr.co.hyo.domain.shop.service.ShopWriteService
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/shops")
@Tag(name = "매장", description = "API Document")
class ShopController(
    private val shopReadService: ShopReadService,
    private val shopWriteService: ShopWriteService,
) {

    @PostMapping
    @ResponseStatus(value = CREATED)
    @Operation(description = "매장 등록")
    fun postShops(
        authentication: Authentication,
        @Valid @RequestBody request: ShopCreateRequest,
    ) {
        shopWriteService.create(dto = request.toSeviceDto())
    }

    @PatchMapping("/name")
    @Operation(description = "매장 이름 변경")
    fun patchShopsName(
        authentication: Authentication,
        @Valid @RequestBody request: ShopChangeNameRequest,
    ) {
        shopWriteService.updateName(dto = request.toSeviceDto())
    }

    @GetMapping("/audits")
    fun getShopsAudits(
        authentication: Authentication,
        @RequestParam("serviceType", defaultValue = "SERVICE_A")
        @Parameter(schema = Schema(description = "서비스 타입", example = "SERVICE_A"))
        serviceType: String,
        @RequestParam("id", defaultValue = "1")
        @Parameter(schema = Schema(description = "매장 ID", example = "1"))
        id: Long,
        @RequestParam("limit", defaultValue = "5")
        @Parameter(schema = Schema(description = "조회할 카운트 수", example = "5"))
        limit: Long,
    ): ResponseEntity<List<ShopAuditDto>> {
        val shopAuditDtos: List<ShopAuditDto> =
            shopReadService.findShopAudits(id = id, serviceType = serviceType, limit = limit)
        return ResponseEntity.ok(shopAuditDtos)
    }
}
