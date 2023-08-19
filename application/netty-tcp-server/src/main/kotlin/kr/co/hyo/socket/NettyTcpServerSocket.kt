package kr.co.hyo.socket

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import jakarta.annotation.PreDestroy
import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.net.InetSocketAddress

@Component
class NettyTcpServerSocket(
    private val nettyTcpServerBootstrap: ServerBootstrap,
    private val nettyTcpServerInetSocketAddress: InetSocketAddress,
) {

    private val logger = KotlinLogging.logger {}
    private var serverChannel: Channel? = null

    fun start() {
        try {
            // ChannelFuture: I/O operation의 결과나 상태를 제공하는 객체
            // 지정한 host, port로 소켓을 바인딩하고 incoming connections을 받도록 준비함
            val serverChannelFuture: ChannelFuture = nettyTcpServerBootstrap
                .bind(nettyTcpServerInetSocketAddress)
                .sync()

            // 서버 소켓이 닫힐때까지 기다림
            serverChannel = serverChannelFuture
                .channel()
                .closeFuture()
                .sync()
                .channel()
        } catch (exception: InterruptedException) {
            logger.error { exception }
        }
    }

    // Bean을 제거하기 전에 해야할 작업이 있을 때 설정
    @PreDestroy
    fun stop() {
        serverChannel?.close()
        serverChannel?.parent()?.closeFuture()
    }
}
