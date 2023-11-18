package kr.co.hyo.config

import io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import kr.co.hyo.webclient.ClientBasedOnWebClient
import kr.co.hyo.webclient.ClientBasedOnWebClientImpl
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import java.time.Duration.ofSeconds

@Configuration
class WebClientConfig {

    private val logger = KotlinLogging.logger {}

    @Bean
    fun webClient(): WebClient =
        WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(createHttpClient()))
            .codecs { clientCodecConfigurer ->
                clientCodecConfigurer.defaultCodecs()
                    .maxInMemorySize(500 * 1024 * 1024) // 500M
            }
            .filter(requestLog())
            .filter(responseLog())
            .build()

    private fun createHttpClient(): HttpClient {
        val connectionProvider = ConnectionProvider
            .builder("custom-connection-provider")
            .maxConnections(100)
            .maxIdleTime(ofSeconds(58))
            .maxLifeTime(ofSeconds(58))
            .pendingAcquireTimeout(ofSeconds(5))
            .pendingAcquireMaxCount(-1)
            .evictInBackground(ofSeconds(30))
            .lifo()
            .metrics(true)
            .build()

        return HttpClient
            .create(connectionProvider)
            .option(CONNECT_TIMEOUT_MILLIS, ofSeconds(5).toMillis().toInt())
            .responseTimeout(ofSeconds(20))
            .compress(true)
            .doOnConnected { connection ->
                connection.addHandlerLast(ReadTimeoutHandler(ofSeconds(20).toSeconds().toInt()))
                connection.addHandlerLast(WriteTimeoutHandler(ofSeconds(20).toSeconds().toInt()))
            }
    }

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

    @Bean
    fun clientBasedOnWebClient(): ClientBasedOnWebClient = ClientBasedOnWebClientImpl(webclient = webClient())
}
