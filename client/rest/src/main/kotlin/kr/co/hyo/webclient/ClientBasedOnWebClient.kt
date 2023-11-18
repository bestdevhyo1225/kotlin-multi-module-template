package kr.co.hyo.webclient

import reactor.core.publisher.Mono

interface ClientBasedOnWebClient {
    fun <T> get(uri: String, clazz: Class<T>): Mono<T?>
}
