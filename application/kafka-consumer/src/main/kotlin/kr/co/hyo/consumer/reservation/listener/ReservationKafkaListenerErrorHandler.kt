package kr.co.hyo.consumer.reservation.listener

import mu.KotlinLogging
import org.apache.kafka.clients.consumer.Consumer
import org.springframework.kafka.listener.KafkaListenerErrorHandler
import org.springframework.kafka.listener.ListenerExecutionFailedException
import org.springframework.messaging.Message
import org.springframework.stereotype.Component

@Component
class ReservationKafkaListenerErrorHandler : KafkaListenerErrorHandler {

    private val logger = KotlinLogging.logger {}

    override fun handleError(message: Message<*>, exception: ListenerExecutionFailedException): Any {
        return message
    }

    override fun handleError(
        message: Message<*>,
        exception: ListenerExecutionFailedException,
        consumer: Consumer<*, *>,
    ): Any {
        logger.error { exception }
        return message // @SendTo 설정한 토픽으로 message 가 전송된다. (재시도 처리)
    }
}
