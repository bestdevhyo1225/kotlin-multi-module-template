package kr.co.hyo.config

import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient
import org.apache.hc.client5.http.impl.async.HttpAsyncClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class HttpClient5Config {

    @Bean
    fun closeableHttpAsyncClient(): CloseableHttpAsyncClient = HttpAsyncClients.createDefault()
}
