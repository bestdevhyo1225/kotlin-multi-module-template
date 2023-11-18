package kr.co.hyo.filter

import kr.co.hyo.httpclient5.ClientBasedOnHttpClient5
import kr.co.hyo.resttemplate.ClientBasedOnRestTemplate
import kr.co.hyo.webclient.ClientBasedOnWebClient
import mu.KotlinLogging
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ExternalApiCallFilter(
    private val clientBasedOnRestTemplate: ClientBasedOnRestTemplate,
    private val clientBasedOnWebClient: ClientBasedOnWebClient,
    private val clientBasedOnHttpClient5: ClientBasedOnHttpClient5,
) : AbstractGatewayFilterFactory<ExternalApiCallFilter.Config>(Config::class.java) {

    class Config

    private val logger = KotlinLogging.logger {}

    override fun apply(config: Config): GatewayFilter =
        GatewayFilter { exchange, chain ->
            val request: ServerHttpRequest = exchange.request

//            // Blocking Call Code (RestTemplate)
//            logger.info { "------------------------------------" }
//
//            val response: String? = clientBasedOnRestTemplate
//                .get(url = "http://localhost:9191/block/${request.id}", clazz = String::class.java)
//
//            logger.info { "external api response: $response" }
//            logger.info { "------------------------------------" }
//
//            return@GatewayFilter chain.filter(exchange)

//            // Blocking Call Code (CloseableHttpAsyncClient)
//            val simpleHttpResponse: SimpleHttpResponse = clientBasedOnHttpClient5
//                .get(uri = "http://localhost:9191/block/${request.id}")
//
//            logger.info { "external api response: ${simpleHttpResponse.bodyText}" }
//            logger.info { "------------------------------------" }
//
//            return@GatewayFilter chain.filter(exchange)

            // Non-Blocking Call Code
            return@GatewayFilter clientBasedOnWebClient
                .get(uri = "http://localhost:9191/block/${request.id}", clazz = String::class.java)
                .flatMap { Mono.just(Void::class.java) }
                .then(chain.filter(exchange))
        }
}
