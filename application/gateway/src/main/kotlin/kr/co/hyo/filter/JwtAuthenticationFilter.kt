package kr.co.hyo.filter

import kr.co.hyo.service.member.MemberAuthenticateService
import mu.KotlinLogging
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono


@Component
class JwtAuthenticationFilter(
    private val memberAuthenticateService: MemberAuthenticateService,
) : AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config>(Config::class.java) {

    class Config

    private val logger = KotlinLogging.logger {}

    companion object {
        private const val BEARER_PREFIX = "Bearer "
        private const val ACCESS_TOKEN_START_INDEX: Int = 7
    }

    override fun apply(config: Config): GatewayFilter =
        GatewayFilter { exchange, chain ->
            val request: ServerHttpRequest = exchange.request

            logger.info { "----- [PRE] requestId: ${request.id} -----" }
            logger.info { "${request.method} ${request.uri}" }

            if (!request.headers.containsKey(AUTHORIZATION)) {
                return@GatewayFilter onError(
                    exchange = exchange,
                    errorMessage = "No Authorization Header Key",
                    httpStatus = UNAUTHORIZED,
                )
            }

            val authorizationHeader: List<String>? = request.headers[AUTHORIZATION]
            if (authorizationHeader.isNullOrEmpty() || !authorizationHeader.first().startsWith(BEARER_PREFIX)) {
                return@GatewayFilter onError(
                    exchange = exchange,
                    errorMessage = "No Authorization Header Value",
                    httpStatus = UNAUTHORIZED,
                )
            }

            val accessToken: String = authorizationHeader.first().substring(startIndex = ACCESS_TOKEN_START_INDEX)
            try {
                memberAuthenticateService.verifyAccessToken(accessToken = accessToken)
            } catch (exception: RuntimeException) {
                return@GatewayFilter onError(
                    exchange = exchange,
                    errorMessage = exception.localizedMessage,
                    httpStatus = UNAUTHORIZED,
                )
            } catch (exception: Exception) {
                return@GatewayFilter onError(
                    exchange = exchange,
                    errorMessage = exception.localizedMessage,
                    httpStatus = INTERNAL_SERVER_ERROR,
                )
            }

            return@GatewayFilter chain
                .filter(exchange)
                .then(Mono.fromRunnable {
                    logger.info { "----- [POST] requestId: ${request.id} -----" }
                })
        }

    private fun onError(exchange: ServerWebExchange, errorMessage: String, httpStatus: HttpStatus): Mono<Void> {
        logger.error(errorMessage)
        val response: ServerHttpResponse = exchange.response
        response.setStatusCode(httpStatus)
        return response.setComplete() // Mono Data
    }
}
