package kr.co.hyo.resolver

import kr.co.hyo.common.util.auth.Auth
import kr.co.hyo.common.util.auth.AuthInfo
import kr.co.hyo.common.util.jwt.JwtParseHelper
import mu.KotlinLogging
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer


class AuthInfoHandlerMethodArgumentResolver(
    private val jwtParseHelper: JwtParseHelper,
) : HandlerMethodArgumentResolver {

    private val logger = KotlinLogging.logger {}

    companion object {
        private const val BEARER_PREFIX = "Bearer "
        private const val ACCESS_TOKEN_START_INDEX: Int = 7
        private const val MEMBER_ID = "memberId"
    }

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        parameter.getParameterAnnotation(Auth::class.java) ?: return false
        return AuthInfo::class.java.isAssignableFrom(parameter.parameterType)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any {
        val authorizationHeader: String? = webRequest.getHeader(AUTHORIZATION)
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            logger.info { "authorizationHeader is not exists." }
            return AuthInfo(accessToken = "", memberId = 0L)
        }
        val accessToken: String = authorizationHeader.substring(startIndex = ACCESS_TOKEN_START_INDEX)
        val memberId: Long = jwtParseHelper.getValue(accessToken = accessToken, key = MEMBER_ID).toLong()
        return AuthInfo(accessToken = accessToken, memberId = memberId)
    }
}
