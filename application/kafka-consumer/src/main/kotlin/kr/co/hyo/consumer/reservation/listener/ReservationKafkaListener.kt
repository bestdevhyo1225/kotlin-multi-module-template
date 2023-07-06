package kr.co.hyo.consumer.reservation.listener

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kr.co.hyo.consumer.common.exception.ListenerExceptionMessage.ACKNOWLEDGMENT_IS_NULL
import kr.co.hyo.consumer.reservation.request.ReservationRequest
import kr.co.hyo.domain.reservation.dto.ReservationCreateDto
import kr.co.hyo.domain.reservation.service.ReservationWriteService
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.AcknowledgingMessageListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class ReservationKafkaListener(
    private val reservationWriteService: ReservationWriteService,
) : AcknowledgingMessageListener<String, String> {

    private val logger = KotlinLogging.logger {}
    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    @KafkaListener(
        topics = ["\${spring.kafka.topics.reservation}"],
        containerFactory = "reservationKafkaListenerContainerFactory",
        errorHandler = "reservationKafkaListenerErrorHandler",
    )
    override fun onMessage(data: ConsumerRecord<String, String>, acknowledgment: Acknowledgment?) {
        jacksonObjectMapper.readValue(data.value(), ReservationRequest::class.java)
            .also {
                reservationWriteService.create(dto = with(receiver = it) {
                    ReservationCreateDto(type = type, memberId = memberId)
                })
            }

        acknowledgment?.acknowledge() ?: throw RuntimeException(ACKNOWLEDGMENT_IS_NULL)

        logger.info { "completed acknowledge (partition: ${data.partition()}, offset: ${data.offset()})" }
    }
}
