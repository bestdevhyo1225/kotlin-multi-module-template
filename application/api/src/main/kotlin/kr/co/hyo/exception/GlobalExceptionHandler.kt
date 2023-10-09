package kr.co.hyo.exception

import kr.co.hyo.common.util.response.ErrorResponse
import kr.co.hyo.common.util.response.FailResponse
import jakarta.validation.ConstraintViolationException
import mu.KotlinLogging
import org.springframework.context.support.DefaultMessageSourceResolvable
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.stream.Collectors

@RestControllerAdvice
class GlobalExceptionHandler {

    private val kotlinLogger = KotlinLogging.logger {}

    @ExceptionHandler(value = [ConstraintViolationException::class])
    fun handle(exception: ConstraintViolationException): ResponseEntity<FailResponse> {
        val message: String? = exception.constraintViolations.stream().toList()[0].message
        val failMessage: String = message ?: "Bad Request"
        kotlinLogger.error { failMessage }
        return ResponseEntity(FailResponse(message = failMessage), BAD_REQUEST)
    }

    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun handle(exception: MethodArgumentNotValidException): ResponseEntity<FailResponse> {
        val message: String = exception.allErrors
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining(" | "))
        kotlinLogger.error { message }
        return ResponseEntity(FailResponse(message = message), BAD_REQUEST)
    }

    @ExceptionHandler(value = [IllegalArgumentException::class])
    fun handle(exception: IllegalArgumentException): ResponseEntity<FailResponse> {
        kotlinLogger.error { exception.localizedMessage }
        return ResponseEntity(FailResponse(message = exception.localizedMessage), BAD_REQUEST)
    }

    @ExceptionHandler(value = [NoSuchElementException::class])
    fun handle(exception: NoSuchElementException): ResponseEntity<FailResponse> {
        kotlinLogger.error { exception.localizedMessage }
        return ResponseEntity(FailResponse(message = exception.localizedMessage), NOT_FOUND)
    }

    @ExceptionHandler(Exception::class)
    fun handle(exception: Exception): ResponseEntity<ErrorResponse> {
        kotlinLogger.error { exception.localizedMessage }
        return ResponseEntity<ErrorResponse>(
            ErrorResponse(message = INTERNAL_SERVER_ERROR.name),
            INTERNAL_SERVER_ERROR,
        )
    }
}
