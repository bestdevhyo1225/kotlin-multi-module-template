package kr.co.hyo.websocket.chatting.controller

import kr.co.hyo.domain.chatting.service.ChattingWriteService
import kr.co.hyo.websocket.chatting.request.ChattingCreateRequest
import mu.KotlinLogging
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller

@Controller
class ChattingController(
    // @EnableWebSocketMessageBroker에 의해서 등록된 Bean으로 브로커로 메시지를 전달한다.
    private val simpleMessagingTemplate: SimpMessagingTemplate,
    private val chattingWriteService: ChattingWriteService,
) {

    private val logger = KotlinLogging.logger {}

    // WebSocketConfig에서 등록한 applicationDestinationPrfixes와 @MessageMapping의 경로가 합쳐진다. -> '/publish/chattings'
    @MessageMapping(value = ["/chattings/{chattingRoomId}/messages"])
    fun chattings(@DestinationVariable chattingRoomId: Long, request: ChattingCreateRequest) {
        // 해당 채팅방을 구독 중인 사용자들에게 메시지를 전달
        simpleMessagingTemplate.convertAndSend("/subscribe/chattings/${chattingRoomId}", request.contents)

        chattingWriteService.saveMessage(dto = request.toDto(chattingRoomId = chattingRoomId))

        logger.info { "chattings convertAndSend (chattingRoomId: $chattingRoomId, request: $request" }
    }
}
