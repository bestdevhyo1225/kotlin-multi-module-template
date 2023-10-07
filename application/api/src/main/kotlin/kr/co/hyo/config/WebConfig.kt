package kr.co.hyo.config

import kr.co.hyo.common.util.jwt.JwtParseHelper
import kr.co.hyo.resolver.AuthInfoHandlerMethodArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val jwtParseHelper: JwtParseHelper,
) : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        super.addArgumentResolvers(resolvers)
        resolvers.add(AuthInfoHandlerMethodArgumentResolver(jwtParseHelper = jwtParseHelper))
    }
}
