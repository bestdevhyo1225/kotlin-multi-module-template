package kr.co.hyo.publisher.post.producer

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kr.co.hyo.publisher.common.producer.AbstractKafkaProducer
import mu.KotlinLogging
import org.apache.kafka.clients.producer.RecordMetadata
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component

@Component
class PostFeedKafkaProducer(
    @Value("\${spring.kafka.topics.post-feed}")
    private val topic: String,
    private val kafkaTemplate: KafkaTemplate<String, String>,
) : AbstractKafkaProducer(), PostFeedProducer {

    private val kotlinLogger = KotlinLogging.logger {}
    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    override fun <T : Any> send(event: T) {
        execute {
            val recordMetadata: RecordMetadata = kafkaTemplate
                .send(topic, jacksonObjectMapper.writeValueAsString(event))
                .get()
                .recordMetadata

            kotlinLogger.info { "send (partition: ${recordMetadata.partition()}, offset: ${recordMetadata.offset()})" }
        }
    }

    override fun <T : Any> sendAsync(event: T) {
        execute {
            kafkaTemplate
                .send(topic, jacksonObjectMapper.writeValueAsString(event))
                .whenCompleteAsync { sendResult: SendResult<String, String>, throwable: Throwable? ->
                    throwable?.let { throw RuntimeException(throwable.localizedMessage) }

                    val recordMetadata: RecordMetadata = sendResult.recordMetadata
                    val partition: Int = recordMetadata.partition()
                    val offset: Long = recordMetadata.offset()

                    kotlinLogger.info { "send async (partition: $partition, offset: $offset)" }
                }
        }
    }
}
