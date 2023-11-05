package kr.co.hyo.api.chatting.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kr.co.hyo.api.chatting.request.ChattingRoomCreateRequest
import kr.co.hyo.api.chatting.request.ChattingRoomMemberCreateRequest
import kr.co.hyo.common.util.auth.Auth
import kr.co.hyo.common.util.auth.AuthInfo
import kr.co.hyo.common.util.page.SimplePage
import kr.co.hyo.domain.chatting.dto.ChattingRoomDto
import kr.co.hyo.domain.chatting.service.ChattingRoomReadService
import kr.co.hyo.domain.chatting.service.ChattingRoomWriteService
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/chattings/rooms")
@Tag(name = "채팅방", description = "API Document")
class ChattingRoomController(
    private val chattingRoomReadService: ChattingRoomReadService,
    private val chattingRoomWriteService: ChattingRoomWriteService,
) {

    @PostMapping
    @ResponseStatus(value = CREATED)
    @Operation(description = "채팅방 생성")
    fun postChattingsRoom(
        @Auth @Parameter(hidden = true) authInfo: AuthInfo,
        @Valid @RequestBody request: ChattingRoomCreateRequest,
    ): ResponseEntity<ChattingRoomDto> {
        val dto: ChattingRoomDto = chattingRoomWriteService.create(createdBy = authInfo.memberId, name = request.name)
        return ResponseEntity
            .created(URI.create("/api/chattings/room/" + dto.id))
            .body(dto)
    }

    @PostMapping("/{chattingRoomId}/members")
    @ResponseStatus(value = CREATED)
    @Operation(description = "채팅방 회원 등록")
    fun postChattingRoomMember(
        @PathVariable @Parameter(schema = Schema(description = "채팅방 ID", example = "1")) chattingRoomId: Long,
        @Valid @RequestBody request: ChattingRoomMemberCreateRequest,
    ) {
        chattingRoomWriteService.addMember(chattingRoomId = chattingRoomId, memberId = request.memberId)
    }

    @GetMapping
    @Operation(description = "채팅방 목록")
    fun getChattingRooms(
        @RequestParam
        @Parameter(schema = Schema(description = "페이지 번호", example = "1"))
        page: Long,
        @RequestParam
        @Parameter(schema = Schema(description = "페이지 사이즈", example = "10"))
        size: Long,
    ): ResponseEntity<SimplePage<ChattingRoomDto>> {
        val pair: Pair<List<ChattingRoomDto>, Long> =
            chattingRoomReadService.findChattingRooms(page = page, size = size)
        return ResponseEntity.ok(SimplePage(items = pair.first, page = page, size = size, total = pair.second))
    }
}
