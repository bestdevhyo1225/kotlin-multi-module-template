package kr.co.hyo.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker // 메시지 브로커가 지원하는 WebSocket 메시지 처리를 활성화
class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    // Handshake 담당의 엔드포인트 설정
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry
            .addEndpoint("/chattings") // WebSocket 연결 요청시, 보낼 Endpoint 지정
            .setAllowedOrigins("*") // 허용할 Origins 설정
            .withSockJS() // 소켓을 지원하지 않는 브라우저라면, SockJS를 사용하도록 설정
    }

    // 메모리 기반의 SimpleMessageBroker 설정
    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        // 메시지 브로커가 구독자들에게 메시지를 전달할 URL 지정 (메시지 구독 요청)
        registry.enableSimpleBroker("/subscribe")
        // 클라이언트가 서버로 메시지 보낼 URL 접두사 지정 (메시지 발행 요청)
        registry.setApplicationDestinationPrefixes("/publish")
    }
}
