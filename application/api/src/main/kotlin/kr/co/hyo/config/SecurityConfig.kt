package kr.co.hyo.config

//import org.springframework.context.annotation.Configuration
//import org.springframework.http.HttpStatus.UNAUTHORIZED
//import org.springframework.security.authentication.AuthenticationManager
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
//import org.springframework.security.web.SecurityFilterChain
//import org.springframework.security.web.authentication.HttpStatusEntryPoint
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

//@Configuration
//@EnableWebSecurity
class SecurityConfig {

//    @Bean
//    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
//        return JwtAuthenticationFilter(memberAuthenticateService = memberAuthenticateService)
//    }

//    @Bean
//    fun filterChain(http: HttpSecurity): SecurityFilterChain {
//        return http
//            .csrf { it.disable() }
//            .cors { it.disable() }
//            .formLogin { it.disable() }
//            .httpBasic { it.disable() }
//            .logout { it.disable() }
//            .authorizeHttpRequests {
//                it.requestMatchers("/api/static/**").permitAll()
//                    .requestMatchers("/api/members/sign-up", "/api/members/sign-in").permitAll()
//                    .requestMatchers("/api/members/**").authenticated()
//                    .requestMatchers("/api/posts/*/search").permitAll()
//                    .requestMatchers("/api/posts/**").authenticated()
//                    .requestMatchers("/api/reservations/**").authenticated()
//                    .requestMatchers("/api/shops/**").authenticated()
//                    .anyRequest().permitAll()
//            }
//            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
//            .exceptionHandling { it.authenticationEntryPoint(HttpStatusEntryPoint(UNAUTHORIZED)) }
//            .build()
//    }

//    @Bean
//    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
//        return authenticationConfiguration.authenticationManager
//    }
}
