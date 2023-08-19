package kr.co.hyo.config

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import kr.co.hyo.handler.NettyTcpServerHandler

class NettyTcpServerChannelInitializer(
    private val nettyTcpServerHandler: NettyTcpServerHandler,
) : ChannelInitializer<SocketChannel>() {

    // 클라이언트 소켓 채널이 생성될 때 호출된다.
    override fun initChannel(ch: SocketChannel) {
        ch.pipeline()
            .addLast(NettyTcpServerDecoder()) // decoder는 @Sharable 이 안 됨, Bean 객체 주입이 안 되고, 매번 새로운 객체 생성해야 함
            .addLast(nettyTcpServerHandler)
    }
}
