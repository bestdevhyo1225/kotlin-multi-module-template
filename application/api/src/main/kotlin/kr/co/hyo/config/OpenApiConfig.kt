package kr.co.hyo.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun openAPI(): OpenAPI {
        val info = Info()
            .version("v1.1.0.RELEASE")
            .title("API")
            .description("API Description")

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
            .addSecurityItem(securityRequirement)
            .components(components)
    }
}
