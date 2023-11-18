package kr.co.hyo.resttemplate

import org.springframework.web.client.RestTemplate

class ClientBasedOnRestTemplateImpl(
    private val restTemplate: RestTemplate,
) : ClientBasedOnRestTemplate {

    override fun <T> get(url: String, clazz: Class<T>): T? {
        return restTemplate.getForObject(url, clazz)
    }
}
