package kr.co.hyo.config

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.timelimiter.TimeLimiterRegistry
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class Resilience4jConfig {

    @Bean
    fun reactiveResilience4JCircuitBreakerFactory(
        circuitBreakerRegistry: CircuitBreakerRegistry,
        timeLimiterRegistry: TimeLimiterRegistry,
    ): ReactiveResilience4JCircuitBreakerFactory =
        ReactiveResilience4JCircuitBreakerFactory(circuitBreakerRegistry, timeLimiterRegistry)
}
