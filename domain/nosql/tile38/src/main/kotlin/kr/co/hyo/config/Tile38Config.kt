package kr.co.hyo.config

import kr.co.hyo.config.properties.Tile38Properties
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.sync.RedisCommands
import io.lettuce.core.codec.StringCodec
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands
import io.lettuce.core.support.ConnectionPoolSupport
import org.apache.commons.pool2.impl.GenericObjectPool
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableConfigurationProperties(value = [Tile38Properties::class])
class Tile38Config(
    private val tile38Properties: Tile38Properties,
) {

    @Bean
    fun tile38RedisClient(): RedisClient =
        RedisClient.create(RedisURI.create(tile38Properties.host, tile38Properties.port))

    @Bean
    fun tile38RedisCommands(): RedisCommands<String, String> =
        tile38RedisClient()
            .connect(StringCodec.UTF8)
            .sync()

    @Bean
    fun tile38RedisPubSubCommands(): RedisPubSubCommands<String, String> =
        tile38RedisClient()
            .connectPubSub(StringCodec.UTF8)
            .sync()

    @Bean
    fun tile38StatefulRedisConnectionPool(): GenericObjectPool<StatefulRedisConnection<String, String>> =
        ConnectionPoolSupport.createGenericObjectPool({ tile38RedisClient().connect() }, getGenericObjectPoolConfig())

    private fun getGenericObjectPoolConfig(): GenericObjectPoolConfig<StatefulRedisConnection<String, String>> {
        val genericObjectPoolConfig = GenericObjectPoolConfig<StatefulRedisConnection<String, String>>()
        genericObjectPoolConfig.minIdle = tile38Properties.pool.minIdle
        genericObjectPoolConfig.maxIdle = tile38Properties.pool.maxIdle
        genericObjectPoolConfig.maxTotal = tile38Properties.pool.maxTotal
        return genericObjectPoolConfig
    }
}
