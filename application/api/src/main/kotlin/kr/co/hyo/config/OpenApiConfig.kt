package kr.co.hyo.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig(
    @Value("\${open-api.service.gateway.url}")
    private val gatewayUrl: String,

    @Value("\${open-api.service.api.url}")
    private val apiUrl: String,
) {

    @Bean
    fun openAPI(): OpenAPI {
        val info = Info()
            .version("v1.1.0.RELEASE")
            .title("API")
            .description("API Description")

        val servers = listOf(
            Server().url(gatewayUrl).description("Gateway"),
            Server().url(apiUrl).description("Api"),
        )

        val jwtSchemeName = "accessToken"
        val securityRequirement = SecurityRequirement()
            .addList(jwtSchemeName)

        val securityScheme = SecurityScheme()
            .name(jwtSchemeName)
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")

        val components = Components()
            .addSecuritySchemes(jwtSchemeName, securityScheme)

        return OpenAPI()
            .info(info)
            .servers(servers)
            .addSecurityItem(securityRequirement)
            .components(components)
    }
}
