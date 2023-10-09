package kr.co.hyo.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisTemplateConfig {

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, String> {
        val stringRedisSerializer = StringRedisSerializer()
        val redisTemplate = RedisTemplate<String, String>()
        redisTemplate.connectionFactory = redisConnectionFactory
        redisTemplate.keySerializer = stringRedisSerializer
        redisTemplate.valueSerializer = stringRedisSerializer
        redisTemplate.hashKeySerializer = stringRedisSerializer
        redisTemplate.hashValueSerializer = stringRedisSerializer
        return redisTemplate
    }

    @Bean
    fun reactiveRedisTemplate(
        reactiveRedisConnectionFactory: ReactiveRedisConnectionFactory,
    ): ReactiveRedisTemplate<String, String> {
        val stringRedisSerializer = StringRedisSerializer()
        val redisSerializationContext: RedisSerializationContext<String, String> = RedisSerializationContext
            .newSerializationContext<String, String>(stringRedisSerializer)
            .value(stringRedisSerializer)
            .build()

        return ReactiveRedisTemplate(reactiveRedisConnectionFactory, redisSerializationContext)
    }
}
