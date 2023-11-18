package kr.co.hyo.config

import kr.co.hyo.httpclient5.ClientBasedOnHttpClient5
import kr.co.hyo.httpclient5.ClientBasedOnHttpClient5Impl
import kr.co.hyo.resttemplate.ClientBasedOnRestTemplate
import kr.co.hyo.resttemplate.ClientBasedOnRestTemplateImpl
import kr.co.hyo.webclient.ClientBasedOnWebClient
import kr.co.hyo.webclient.ClientBasedOnWebClientImpl
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class ClientConfig {

    @Bean
    fun clientBasedOnRestTemplate(restTemplate: RestTemplate): ClientBasedOnRestTemplate =
        ClientBasedOnRestTemplateImpl(restTemplate = restTemplate)

    @Bean
    fun clientBasedOnWebClient(webClient: WebClient): ClientBasedOnWebClient =
        ClientBasedOnWebClientImpl(webclient = webClient)

    @Bean
    fun clientBasedOnHttpClient5(closeableHttpAsyncClient: CloseableHttpAsyncClient): ClientBasedOnHttpClient5 =
        ClientBasedOnHttpClient5Impl(closeableHttpAsyncClient = closeableHttpAsyncClient)
}
