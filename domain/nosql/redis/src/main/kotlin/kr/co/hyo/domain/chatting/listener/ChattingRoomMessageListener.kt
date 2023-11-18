package kr.co.hyo.domain.chatting.listener

import mu.KotlinLogging
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.stereotype.Component

@Component
class ChattingRoomMessageListener(

) : MessageListener {

    private val logger = KotlinLogging.logger {}

    override fun onMessage(message: Message, pattern: ByteArray?) {


        TODO("Not yet implemented")
    }
}
