package kr.co.hyo.config

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelOption.SO_BACKLOG
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LogLevel.DEBUG
import io.netty.handler.logging.LoggingHandler
import kr.co.hyo.handler.NettyTcpServerHandler
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.InetSocketAddress


@Configuration
class NettyTcpServerConfig(
    @Value("\${server.host}")
    private val host: String,

    @Value("\${server.port}")
    private val port: Int,

    @Value("\${server.netty.boss-count}")
    private val bossCount: Int,

    @Value("\${server.netty.keep-alive}")
    private val keepAlive: Boolean,

    @Value("\${server.netty.backlog}")
    private val backlog: Int,

    private val nettyTcpServerHandler: NettyTcpServerHandler,
) {

    @Bean
    fun nettyTcpServerBootstrap(): ServerBootstrap {
        val serverBootstrap = ServerBootstrap()
        serverBootstrap
            .group(nettyTcpServerBossGroup(), nettyTcpServerWorkerGroup())
            // NioServerSocketChannel: incoming connections를 수락하기 위해 새로운 Channel을 객체화할 때 사용
            .channel(NioServerSocketChannel::class.java)
            .handler(LoggingHandler(DEBUG))
            // ChannelInitializer: 새로운 Channel을 구성할 때 사용되는 특별한 handler. 주로 ChannelPipeline으로 구성
            .childHandler(nettyTcpServerChannelInitializer())
            // SO_BACKLOG: 동시에 수용 가능한 최대 incoming connections 개수
            .option(SO_BACKLOG, backlog)
        return serverBootstrap
    }

    // boss: incoming connection을 수락하고, 수락한 connection을 worker에게 등록(register)
    @Bean(destroyMethod = "shutdownGracefully")
    fun nettyTcpServerBossGroup(): NioEventLoopGroup = NioEventLoopGroup(bossCount)

    // worker: boss가 수락한 연결의 트래픽 관리
    @Bean(destroyMethod = "shutdownGracefully")
    fun nettyTcpServerWorkerGroup(): NioEventLoopGroup =
        // workerCount를 지정하지 않으면 default 값 사용 (Worker 스레드 풀의 default 값 = CPU 코어 * 2)
        // DEFAULT_EVENT_LOOP_THREADS = Math.max(1, SystemPropertyUtil.getInt(
        //                "io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2))
        NioEventLoopGroup()

    // IP 소켓 주소(IP 주소, Port 번호)를 구현
    // 도메인 이름으로 객체 생성 가능
    @Bean
    fun nettyTcpServerInetSocketAddress(): InetSocketAddress = InetSocketAddress(host, port)

    // NettyChannel 초기화 관련
    @Bean
    fun nettyTcpServerChannelInitializer(): NettyTcpServerChannelInitializer =
        NettyTcpServerChannelInitializer(nettyTcpServerHandler = nettyTcpServerHandler)
}
