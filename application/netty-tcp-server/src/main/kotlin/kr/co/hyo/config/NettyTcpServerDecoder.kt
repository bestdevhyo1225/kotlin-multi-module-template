package kr.co.hyo.config

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import kr.co.hyo.config.NettyTcpServerConstants.DATA_LENGTH

class NettyTcpServerDecoder : ByteToMessageDecoder() {

    // 정해진 DATA_LENGTH 만큼 데이터가 들어올 때까지 기다린다.
    override fun decode(ctx: ChannelHandlerContext?, `in`: ByteBuf, out: MutableList<Any>) {
        if (`in`.readableBytes() < DATA_LENGTH) {
            return
        }
        out.add(`in`.readBytes(DATA_LENGTH))
    }
}
