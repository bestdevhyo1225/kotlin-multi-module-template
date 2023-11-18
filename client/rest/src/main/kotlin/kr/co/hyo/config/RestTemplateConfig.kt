package kr.co.hyo.config

import kr.co.hyo.resttemplate.ClientBasedOnRestTemplate
import kr.co.hyo.resttemplate.ClientBasedOnRestTemplateImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class RestTemplateConfig {

    @Bean
    fun restTemplate(): RestTemplate = RestTemplate()

    @Bean
    fun clientBasedOnRestTemplate(): ClientBasedOnRestTemplate =
        ClientBasedOnRestTemplateImpl(restTemplate = restTemplate())
}
