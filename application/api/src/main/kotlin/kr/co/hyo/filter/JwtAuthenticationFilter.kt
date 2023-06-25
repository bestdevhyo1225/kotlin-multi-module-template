package kr.co.hyo.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.co.hyo.api.member.service.MemberAuthenticateService
import mu.KotlinLogging
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val memberAuthenticateService: MemberAuthenticateService,
) : OncePerRequestFilter() {

    private val kotlinLogger = KotlinLogging.logger {}

    companion object {
        private const val BEARER_PREFIX = "Bearer "
        private const val ACCESS_TOKEN_START_INDEX: Int = 7
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val authorizationHeader: String? = request.getHeader(AUTHORIZATION)

        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response)
            return
        }

        try {
            val accessToken: String = authorizationHeader.substring(startIndex = ACCESS_TOKEN_START_INDEX)
            val authentication: UsernamePasswordAuthenticationToken =
                memberAuthenticateService.create(accessToken = accessToken)

            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

            SecurityContextHolder.getContext().authentication = authentication

            filterChain.doFilter(request, response)
        } catch (exception: Exception) {
            kotlinLogger.error { exception.localizedMessage }
            setErrorResponse(response = response)
        }
    }

    private fun setErrorResponse(response: HttpServletResponse) {
        response.status = UNAUTHORIZED.value()
        response.contentType = APPLICATION_JSON_VALUE
    }
}
