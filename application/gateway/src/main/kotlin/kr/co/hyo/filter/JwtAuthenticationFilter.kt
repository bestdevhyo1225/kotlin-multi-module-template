package kr.co.hyo.filter

import kr.co.hyo.common.util.jwt.JwtParseHelper
import kr.co.hyo.domain.member.service.MemberTokenReadService
import kr.co.hyo.exception.InvalidJwtTokenException
import mu.KotlinLogging
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono


@Component
class JwtAuthenticationFilter(
    private val jwtParseHelper: JwtParseHelper,
    private val memberTokenReadService: MemberTokenReadService,
) : AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config>(Config::class.java) {

    class Config

    private val logger = KotlinLogging.logger {}

    companion object {
        private const val BEARER_PREFIX = "Bearer "
        private const val ACCESS_TOKEN_START_INDEX: Int = 7
        private const val MEMBER_ID = "memberId"
    }

    override fun apply(config: Config): GatewayFilter =
        GatewayFilter { exchange, chain ->
            val request: ServerHttpRequest = exchange.request

            logger.info { "[PRE] requestId: ${request.id}" }
            logger.info { "--> ${request.method} ${request.uri}" }

            if (!request.headers.containsKey(AUTHORIZATION)) {
                throw InvalidJwtTokenException(message = "헤더 인증 정보 미입력")
            }

            val authorizationHeader: List<String>? = request.headers[AUTHORIZATION]
            if (authorizationHeader.isNullOrEmpty() || !authorizationHeader.first().startsWith(BEARER_PREFIX)) {
                throw InvalidJwtTokenException(message = "액세스 토큰 미입력")
            }

            val accessToken: String = authorizationHeader.first().substring(startIndex = ACCESS_TOKEN_START_INDEX)
            try {
                jwtParseHelper.verify(accessToken = accessToken)
            } catch (exception: IllegalArgumentException) {
                throw InvalidJwtTokenException(message = exception.localizedMessage)
            }

            return@GatewayFilter memberTokenReadService
                .verifyBlackListToken(
                    memberId = jwtParseHelper.getValue(accessToken = accessToken, key = MEMBER_ID).toLong(),
                    accessToken = accessToken,
                )
                .then(chain.filter(exchange))
                .then(Mono.fromRunnable {
                    logger.info { "[POST] requestId: ${request.id}" }
                    logger.info { "<-- ${exchange.response.statusCode}" }
                })
        }
}
