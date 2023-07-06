package kr.co.hyo.consumer.post.listener

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kr.co.hyo.consumer.common.exception.ListenerExceptionMessage.ACKNOWLEDGMENT_IS_NULL
import kr.co.hyo.consumer.post.request.PostFeedRequest
import kr.co.hyo.domain.post.service.PostFeedWriteService
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.AcknowledgingMessageListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class PostFeedKafkaListener(
    private val postFeedWriteService: PostFeedWriteService,
) : AcknowledgingMessageListener<String, String> {

    private val logger = KotlinLogging.logger {}
    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    @KafkaListener(
        topics = ["\${spring.kafka.topics.post-feed}"],
        containerFactory = "postFeedKafkaListenerContainerFactory",
        errorHandler = "postFeedKafkaListenerErrorHandler",
    )
    override fun onMessage(data: ConsumerRecord<String, String>, acknowledgment: Acknowledgment?) {
        jacksonObjectMapper.readValue(data.value(), PostFeedRequest::class.java)
            .also { postFeedWriteService.create(memberId = it.followerId, postId = it.postId) }

        acknowledgment?.acknowledge() ?: throw RuntimeException(ACKNOWLEDGMENT_IS_NULL)

        logger.info { "completed acknowledge (partition: ${data.partition()}, offset: ${data.offset()})" }
    }
}
