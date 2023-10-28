package kr.co.hyo.controller

import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import kr.co.hyo.exception.ServiceCallNotPermittedException
import kr.co.hyo.exception.ServiceConnectException
import kr.co.hyo.exception.ServiceTimeoutException
import mu.KotlinLogging
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import java.net.ConnectException
import java.util.concurrent.TimeoutException

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
        val exception: Throwable? =
            exchange.getAttribute(ServerWebExchangeUtils.CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR)

        when (exception) {
            // 서비스 연결 예외 발생
            is ConnectException -> {
                throw ServiceConnectException(message = "서비스 연결에 문제가 있어 응답을 반환할 수 없습니다.")
            }
            // Circuit Breaker 가 Open 되었을때, 발생하는 예외
            is CallNotPermittedException -> {
                throw ServiceCallNotPermittedException(message = "서비스를 일시적으로 이용할 수 없습니다. 잠시만 기다려 주세요.")
            }
            // 서비스 타임아웃 예외 발생
            is TimeoutException -> {
                throw ServiceTimeoutException(message = "서비스를 요청하는데, 타임아웃이 발생했습니다.")
            }
            // 그외 나머지 예외 발생
            else -> throw Exception(exception?.localizedMessage ?: "")
        }
    }
}
