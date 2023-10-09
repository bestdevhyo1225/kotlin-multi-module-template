package kr.co.hyo.exception

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.validation.ConstraintViolationException
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.mono
import kr.co.hyo.common.util.response.ErrorResponse
import mu.KotlinLogging
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.NOT_FOUND
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
            is NoSuchElementException, is IllegalArgumentException, is ConstraintViolationException,
            is MethodArgumentNotValidException, is InvalidJwtTokenException,
            -> {
                ErrorResponse(message = ex.localizedMessage)
            }

            else -> ErrorResponse(message = "Internal Server Error")
        }

        with(receiver = exchange.response) {
            headers.contentType = MediaType.APPLICATION_JSON

            when (ex) {
                is IllegalArgumentException, is ConstraintViolationException, is MethodArgumentNotValidException,
                -> {
                    BAD_REQUEST.also { statusCode = it }
                }

                is InvalidJwtTokenException -> UNAUTHORIZED.also { statusCode = it }
                is NoSuchElementException -> NOT_FOUND.also { statusCode = it }
                else -> INTERNAL_SERVER_ERROR.also { statusCode = it }
            }

            val dataBuffer = bufferFactory().wrap(objectMapper.writeValueAsBytes(errorResponse))

            writeWith(dataBuffer.toMono()).awaitSingle()
        }
    }
}
