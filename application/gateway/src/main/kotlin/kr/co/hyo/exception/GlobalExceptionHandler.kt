package kr.co.hyo.exception

import com.fasterxml.jackson.databind.ObjectMapper
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
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Configuration
class GlobalExceptionHandler(
    private val objectMapper: ObjectMapper,
) : ErrorWebExceptionHandler {

    private val logger = KotlinLogging.logger {}

    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> = mono {
        // 코루틴 코드를 작성할 수 있다.
        logger.error { ex.message }

        val errorResponse = when (ex) {
            is IllegalArgumentException, is ConstraintViolationException, is MethodArgumentNotValidException,
            is ServiceJwtTokenException, is NoSuchElementException, is ServiceConnectException,
            is ServiceCallNotPermittedException, is ServiceTimeoutException,
            -> ErrorResponse(message = ex.localizedMessage)

            else -> ErrorResponse(message = INTERNAL_SERVER_ERROR.name)
        }

        with(receiver = exchange.response) {
            // Set Response Header
            headers.contentType = MediaType.APPLICATION_JSON

            // Set Response Status
            statusCode = when (ex) {
                is IllegalArgumentException, is ConstraintViolationException, is MethodArgumentNotValidException,
                -> BAD_REQUEST

                is ServiceJwtTokenException -> UNAUTHORIZED
                is NoSuchElementException -> NOT_FOUND
                is ServiceConnectException -> BAD_GATEWAY
                is ServiceCallNotPermittedException -> SERVICE_UNAVAILABLE
                is ServiceTimeoutException -> GATEWAY_TIMEOUT
                else -> INTERNAL_SERVER_ERROR
            }

            val dataBuffer = bufferFactory().wrap(objectMapper.writeValueAsBytes(errorResponse))

            writeWith(dataBuffer.toMono()).awaitSingle()
        }
    }
}
