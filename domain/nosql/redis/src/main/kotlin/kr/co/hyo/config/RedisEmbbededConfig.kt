package kr.co.hyo.config

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.util.StringUtils
import redis.embedded.RedisServer
import java.io.BufferedReader
import java.io.InputStreamReader

@Configuration
@Profile(value = ["default", "test", "local", "dev"])
class RedisEmbbededConfig {

    private val kotlinLogger = KotlinLogging.logger {}

    private lateinit var embeddedRedisServer: RedisServer

    private val host = "localhost"
    private val port: Int = 23680
    private var availablePort: Int = port

    @PostConstruct
    fun startEmbeddedRedisServer() {
        availablePort = if (isRedisRunning()) findAvailablePort() else port
        embeddedRedisServer = RedisServer(availablePort)
        embeddedRedisServer.start()
        kotlinLogger.info { "RedisEmbeddedServer(availablePort: $availablePort)" }
    }

    @PreDestroy
    fun stopEmbeddedRedisServer() {
        embeddedRedisServer.stop()
    }

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        kotlinLogger.info { "LettuceConnectionFactory(host: $host, availablePort: $availablePort)" }
        return LettuceConnectionFactory(host, availablePort)
    }

    /**
     * Embedded Redis가 현재 실행중인지 확인
     */
    private fun isRedisRunning(): Boolean {
        return isRunning(executeGrepProcessCommand(port))
    }

    /**
     * 현재 PC/서버에서 사용가능한 포트 조회
     */
    private fun findAvailablePort(): Int {
        for (port in 10000..65535) {
            val process = executeGrepProcessCommand(port)
            if (!isRunning(process)) {
                return port
            }
        }
        throw IllegalArgumentException("Not Found Available port: 10000 ~ 65535")
    }

    /**
     * 해당 port를 사용중인 프로세스 확인하는 sh 실행
     */
    private fun executeGrepProcessCommand(port: Int): Process {
        val command = String.format("netstat -nat | grep LISTEN|grep %d", port)
        val shell = arrayOf("/bin/sh", "-c", command)
        return Runtime.getRuntime().exec(shell)
    }

    /**
     * 해당 Process가 현재 실행중인지 확인
     */
    private fun isRunning(process: Process): Boolean {
        var line: String?
        val pidInfo = StringBuilder()
        try {
            BufferedReader(InputStreamReader(process.inputStream)).use { input ->
                while (input.readLine().also { line = it } != null) {
                    pidInfo.append(line)
                }
            }
        } catch (exception: Exception) {
            kotlinLogger.error { exception.localizedMessage }
        }
        return StringUtils.hasLength(pidInfo.toString())
    }
}
