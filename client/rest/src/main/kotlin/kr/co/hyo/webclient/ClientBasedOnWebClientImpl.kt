package kr.co.hyo.webclient

import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

class ClientBasedOnWebClientImpl(
    private val webclient: WebClient,
) : ClientBasedOnWebClient {

    override fun <T> get(uri: String, clazz: Class<T>): Mono<T?> {
        return webclient
            .get()
            .uri(uri)
            .accept(APPLICATION_JSON)
            .retrieve()
            .bodyToMono(clazz)
    }
}
