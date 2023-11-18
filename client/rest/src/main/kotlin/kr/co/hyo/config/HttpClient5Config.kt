package kr.co.hyo.config

import kr.co.hyo.httpclient5.ClientBasedOnHttpClient5
import kr.co.hyo.httpclient5.ClientBasedOnHttpClient5Impl
import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient
import org.apache.hc.client5.http.impl.async.HttpAsyncClientBuilder
import org.apache.hc.client5.http.impl.async.HttpAsyncClients
import org.apache.hc.core5.http.Header
import org.apache.hc.core5.util.Timeout
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class HttpClient5Config {

    @Bean
    fun closeableHttpAsyncClient(): CloseableHttpAsyncClient {
        val config: RequestConfig = RequestConfig.custom()
            .setConnectTimeout(Timeout.ofSeconds(7))
            .setResponseTimeout(Timeout.ofSeconds(7))
            .setConnectionRequestTimeout(Timeout.ofSeconds(7))
            .build()

        return HttpAsyncClientBuilder
            .create()
            .setDefaultRequestConfig(config)
            .build()
    }

    @Bean
    fun clientBasedOnHttpClient5(): ClientBasedOnHttpClient5 =
        ClientBasedOnHttpClient5Impl(closeableHttpAsyncClient = closeableHttpAsyncClient())
}
