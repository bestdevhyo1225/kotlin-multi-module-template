package kr.co.hyo.filter

import mu.KotlinLogging
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GatewayAuthFilter : AbstractGatewayFilterFactory<GatewayAuthFilter.Config>(Config::class.java) {

    private val logger = KotlinLogging.logger {}

    class Config

    override fun apply(config: Config): GatewayFilter =
        GatewayFilter { exchange, chain ->
            val request: ServerHttpRequest = exchange.request

            logger.info { "----- [PRE] requestId: ${request.id} -----" }
            logger.info { "${request.method} ${request.uri}" }

            return@GatewayFilter chain
                .filter(exchange)
                .then(Mono.fromRunnable {
                    logger.info { "----- [POST] requestId: ${request.id} -----" }
                })
        }
}
