package kr.co.hyo.handler

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelFutureListener.CLOSE
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import kr.co.hyo.config.NettyTcpServerConstants.DATA_LENGTH
import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.net.SocketAddress

@Component
@Sharable
class NettyTcpServerHandler : ChannelInboundHandlerAdapter() {

    private val logger = KotlinLogging.logger {}
    private var byteBuf: ByteBuf? = null

    // 핸들러가 생성될 때 호출되는 메소드
    override fun handlerAdded(ctx: ChannelHandlerContext) {
        byteBuf = ctx.alloc().buffer(DATA_LENGTH)
    }

    // 핸들러가 제거될 때 호출되는 메소드
    override fun handlerRemoved(ctx: ChannelHandlerContext) {
        byteBuf = null
    }

    // 클라이언트와 연결되어 트래픽을 생성할 준비가 되었을 때 호출되는 메소드
    override fun channelActive(ctx: ChannelHandlerContext) {
        val remoteAddress: SocketAddress = ctx.channel().remoteAddress()
        logger.info { "remote address: $remoteAddress" }
    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        val byteBufMsg = msg as ByteBuf
        (byteBuf ?: throw RuntimeException("Byte Buffer is null.")).writeBytes(byteBufMsg) // 클라이언트에서 보내는 데이터가 축적된다.
        byteBufMsg.release()

        val channelFuture: ChannelFuture = ctx.writeAndFlush(byteBuf)
        channelFuture.addListener(CLOSE)
    }
}
