package kr.co.hyo.config

import io.lettuce.core.ReadFrom.REPLICA_PREFERRED
import io.lettuce.core.cluster.ClusterClientOptions
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions
import kr.co.hyo.config.RedisMode.Cluster
import kr.co.hyo.config.RedisMode.Replication
import kr.co.hyo.config.RedisMode.Standalone
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.connection.RedisClusterConfiguration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.RedisStaticMasterReplicaConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import java.time.Duration

@Configuration
@EnableCaching(proxyTargetClass = true)
@Profile(value = ["prod"])
class RedisConfig(
    @Value("\${spring.redis.mode}")
    private val mode: RedisMode,

    @Value("\${spring.redis.nodes}")
    private val nodes: List<String>,
) {

    private val logger = KotlinLogging.logger {}

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        val lettuceConnectionFactory: LettuceConnectionFactory = when (mode) {
            Standalone -> {
                logger.info { "redis standalone mode" }

                val (host: String, port: Int) = getHostAndPort()
                LettuceConnectionFactory(RedisStandaloneConfiguration(host, port))
            }

            Replication -> {
                logger.info { "redis primary-replica mode" }

                val (host: String, port: Int) = getHostAndPort()
                val staticMasterReplicaConfiguration = RedisStaticMasterReplicaConfiguration(host, port)
                setReplicaNodes(staticMasterReplicaConfiguration = staticMasterReplicaConfiguration)
                LettuceConnectionFactory(staticMasterReplicaConfiguration, lettuceClusterClientConfig())
            }

            Cluster -> {
                logger.info { "redis cluster mode" }

                LettuceConnectionFactory(RedisClusterConfiguration(nodes), lettuceClusterClientConfig())
            }
        }

        return lettuceConnectionFactory.apply {
            // RedisClient.connect 에서 블록킹이 발생하는데, EagerInitialization 를 true로 처리하여 해결할 수 있다.
            eagerInitialization = true
        }
    }

    private fun setReplicaNodes(staticMasterReplicaConfiguration: RedisStaticMasterReplicaConfiguration) {
        nodes.drop(n = 1).forEach { replicaNode ->
            val (replicaHost: String, replicaPort: String) = replicaNode.split(":")
            staticMasterReplicaConfiguration.addNode(replicaHost, replicaPort.toInt())
        }
    }

    private fun lettuceClusterClientConfig(): LettuceClientConfiguration {
        // ClusterTopology를 설정하면, 정기적으로 토폴로지를 확인하게 된다.
        // 즉, Cluster Topology 정보가 변경되면, 자동으로 정보를 가져올 수 있는 설정이다.
        val topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
            .enablePeriodicRefresh(Duration.ofMinutes(10))
            .enableAllAdaptiveRefreshTriggers()
            .build()

        val clientOptions = ClusterClientOptions.builder()
            .topologyRefreshOptions(topologyRefreshOptions)
            .build()

        return LettuceClientConfiguration.builder()
            .clientName("redis-cluster-client")
            .readFrom(REPLICA_PREFERRED)
            .clientOptions(clientOptions)
            .build()
    }

    private fun getHostAndPort(): Pair<String, Int> {
        val splits: List<String> = nodes.first().split(":")
        return Pair(first = splits[0], second = splits[1].toInt())
    }
}
