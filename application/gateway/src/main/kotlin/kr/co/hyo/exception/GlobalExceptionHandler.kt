package kr.co.hyo.exception

import com.fasterxml.jackson.databind.ObjectMapper
import com.sun.jdi.connect.spi.ClosedConnectionException
import jakarta.validation.ConstraintViolationException
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.mono
import kr.co.hyo.common.util.response.ErrorResponse
import mu.KotlinLogging
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus.BAD_GATEWAY
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.GATEWAY_TIMEOUT
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.MediaType
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.reactive.function.client.WebClientRequestException
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.net.ConnectException
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeoutException

@Configuration
class GlobalExceptionHandler(
    private val objectMapper: ObjectMapper,
) : ErrorWebExceptionHandler {

    private val logger = KotlinLogging.logger {}

    companion object {
        private const val CONNECTION_EX_MESSAGE = "서비스 연결에 문제가 있어 응답을 반환할 수 없습니다."
        private const val CALL_NOT_PERMITTED_EX_MESSAGE = "서비스를 일시적으로 이용할 수 없습니다. 잠시만 기다려 주세요."
        private const val TIMEOUT_EX_MESSAGE = "서비스 요청에 대한 타임아웃이 발생하여 응답을 반환할 수 없습니다."
    }

    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> = mono {
        // 코루틴 코드를 작성할 수 있다.
        logger.error { ex.message }

        with(receiver = exchange.response) {
            // Set Response Header
            headers.contentType = MediaType.APPLICATION_JSON

            // Set StatusCode & ErrorResponse
            val errorResponse: ErrorResponse
            when (ex) {
                // 400
                is IllegalArgumentException, is ConstraintViolationException, is MethodArgumentNotValidException,
                -> {
                    statusCode = BAD_REQUEST
                    errorResponse = ErrorResponse(message = ex.localizedMessage)
                }
                // 401
                is ServiceJwtException -> {
                    statusCode = UNAUTHORIZED
                    errorResponse = ErrorResponse(message = ex.localizedMessage)
                }
                // 404
                is NoSuchElementException -> {
                    statusCode = NOT_FOUND
                    errorResponse = ErrorResponse(message = ex.localizedMessage)
                }
                // 502
                is ConnectException, is ClosedConnectionException, is WebClientRequestException -> {
                    statusCode = BAD_GATEWAY
                    errorResponse = ErrorResponse(message = CONNECTION_EX_MESSAGE)
                }
                // 503
                is ServiceCallNotPermittedException -> {
                    statusCode = SERVICE_UNAVAILABLE
                    errorResponse = ErrorResponse(message = CALL_NOT_PERMITTED_EX_MESSAGE)
                }
                // 504
                is TimeoutException, is ExecutionException -> {
                    statusCode = GATEWAY_TIMEOUT
                    errorResponse = ErrorResponse(message = TIMEOUT_EX_MESSAGE)
                }
                // 500
                else -> {
                    statusCode = INTERNAL_SERVER_ERROR
                    errorResponse = ErrorResponse(message = INTERNAL_SERVER_ERROR.name)
                }
            }

            val dataBuffer = bufferFactory().wrap(objectMapper.writeValueAsBytes(errorResponse))

            writeWith(dataBuffer.toMono()).awaitSingle()
        }
    }
}
