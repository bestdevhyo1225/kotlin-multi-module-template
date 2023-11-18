package kr.co.hyo.config

import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import kotlin.math.log

@Configuration
class WebClientConfig {

    private val logger = KotlinLogging.logger {}

    @Bean
    fun webClient(): WebClient = WebClient.builder()
        .filter(requestLog())
        .filter(responseLog())
        .build()

    private fun requestLog(): ExchangeFilterFunction =
        ExchangeFilterFunction.ofRequestProcessor { clientRequest ->
            logger.info { "========== Request ==========" }
            logger.info { "Request: ${clientRequest.method()} ${clientRequest.url()}" }
            clientRequest.headers().forEach { logger.info { "${it.key} : ${it.value}" } }
            logger.info { "========== Request ==========" }
            Mono.just(clientRequest)
        }

    private fun responseLog(): ExchangeFilterFunction =
        ExchangeFilterFunction.ofResponseProcessor { clientResponse ->
            logger.info { "========== Response ==========" }
            logger.info { "Response status: ${clientResponse.statusCode()}" }
            clientResponse.headers().asHttpHeaders().forEach { logger.info { "${it.key} : ${it.value}" } }
            logger.info { "========== Response ==========" }
            Mono.just(clientResponse)
        }
}
