package kr.co.hyo.listener

import kr.co.hyo.socket.NettyTcpServerSocket
import mu.KotlinLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
class NettyApplicationReadEventListener(
    private val nettyTcpServerSocket: NettyTcpServerSocket,
) : ApplicationListener<ApplicationReadyEvent> {

    private val logger = KotlinLogging.logger {}

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        logger.info { "start tcp server application..." }
        nettyTcpServerSocket.start()
    }
}
