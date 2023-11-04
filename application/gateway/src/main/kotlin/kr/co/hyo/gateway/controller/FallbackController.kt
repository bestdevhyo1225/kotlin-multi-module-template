package kr.co.hyo.gateway.controller

import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import kr.co.hyo.exception.ServiceCallNotPermittedException
import mu.KotlinLogging
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils.CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange

@RestController
@RequestMapping("/fallback")
class FallbackController {

    private val logger = KotlinLogging.logger {}

    @GetMapping
    fun getFallback(exchange: ServerWebExchange) {
        handleException(exchange)
    }

    @PostMapping
    fun postFallback(exchange: ServerWebExchange) {
        handleException(exchange)
    }

    @PutMapping
    fun putFallback(exchange: ServerWebExchange) {
        handleException(exchange)
    }

    @PatchMapping
    fun patchFallback(exchange: ServerWebExchange) {
        handleException(exchange)
    }

    @DeleteMapping
    fun deleteFallback(exchange: ServerWebExchange) {
        handleException(exchange)
    }

    private fun handleException(exchange: ServerWebExchange) {
        when (val exception: Throwable? = exchange.getAttribute(CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR)) {
            // Circuit Breaker 가 Open 되었을때, 발생하는 예외
            is CallNotPermittedException -> {
                // exception 을 그냥 던지면, GlobalExceptionHandler 클래스에서 처리하지 않는 부분이 있어 Custom 예외 클래스를 생성했음.
                throw ServiceCallNotPermittedException(message = exception.localizedMessage)
            }
            // 그외 나머지 예외 발생 (연결 실패, 요청 타임아웃)
            is Exception -> {
                throw exception
            }
        }
    }
}
