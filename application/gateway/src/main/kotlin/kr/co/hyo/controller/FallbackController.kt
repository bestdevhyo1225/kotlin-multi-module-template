package kr.co.hyo.controller

import kr.co.hyo.common.util.response.ErrorResponse
import mu.KotlinLogging
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono


@RestController
class FallbackController {

    private val logger = KotlinLogging.logger {}

    @GetMapping("/fallback")
    fun fallback(exchange: ServerWebExchange): Mono<ErrorResponse> {
        val exception: Throwable? =
            exchange.getAttribute(ServerWebExchangeUtils.CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR)

        logger.error { "fallback: ${exception?.localizedMessage ?: "INTERNAL_SERVER_ERROR"}" }

        return Mono.just(ErrorResponse(message = "일시적으로 서비스를 이용할 수 없습니다. 잠시후에 다시 시도해주세요."))
    }
}
