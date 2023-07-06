package kr.co.hyo.config

import org.apache.kafka.clients.consumer.ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_INSTANCE_ID_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_RECORDS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.consumer.CooperativeStickyAssignor
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ContainerProperties.AckMode
import java.net.InetAddress
import java.util.UUID

@Configuration
@EnableKafka
class KafkaConsumerConfig(
    @Value("\${spring.kafka.consumer.bootstrap-servers}")
    private val bootstrapServers: String,

    @Value("\${spring.kafka.consumer.auto-offset-reset}")
    private val autoOffsetReset: String,

    @Value("\${spring.kafka.consumer.session-timeout}")
    private val sessionTimeout: Int,

    @Value("\${spring.kafka.consumer.heartbeat-interval}")
    private val heartbeatInterval: Int,

    @Value("\${spring.kafka.consumer.max-poll-interval}")
    private val maxPollInterval: Int,

    @Value("\${spring.kafka.consumer.max-poll-records}")
    private val maxPollRecords: Int,

    @Value("\${spring.kafka.consumer.allow-auto-create-topics}")
    private val allowAutoCreateTopics: Boolean,

    @Value("\${spring.kafka.consumer.group-id}")
    private val groupId: String,
) {

    @Bean
    fun postFeedKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
        val containerFactory = ConcurrentKafkaListenerContainerFactory<String, String>()
        containerFactory.consumerFactory = DefaultKafkaConsumerFactory(postFeedConfigConsumerProps())
        containerFactory.containerProperties.ackMode = AckMode.MANUAL_IMMEDIATE // 즉시 수동 커밋
        return containerFactory
    }

    @Bean
    fun reservationKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
        val containerFactory = ConcurrentKafkaListenerContainerFactory<String, String>()
        containerFactory.consumerFactory = DefaultKafkaConsumerFactory(reservationConfigConsumerProps())
        containerFactory.containerProperties.ackMode = AckMode.MANUAL_IMMEDIATE // 즉시 수동 커밋
        return containerFactory
    }

    private fun postFeedConfigConsumerProps(): Map<String, Any> {
        val props: MutableMap<String, Any> = mutableMapOf()
        props[BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        props[ENABLE_AUTO_COMMIT_CONFIG] = false // 수동 커밋
        props[AUTO_OFFSET_RESET_CONFIG] = autoOffsetReset
        props[SESSION_TIMEOUT_MS_CONFIG] = sessionTimeout
        props[HEARTBEAT_INTERVAL_MS_CONFIG] = heartbeatInterval
        props[MAX_POLL_INTERVAL_MS_CONFIG] = maxPollInterval
        props[MAX_POLL_RECORDS_CONFIG] = maxPollRecords
        props[ALLOW_AUTO_CREATE_TOPICS_CONFIG] = allowAutoCreateTopics
        /*
         * 스태틱 멤버십 -> 컨슈머 그룹 내에서 컨슈머가 재시작 등으로 그룹에서 나갔다가 다시 합류하더라도 리밸런싱이 일어나지 않도록 한다.
         * 스태틱 멤버십 적용을 위해 'GROUP_INSTANCE_ID' 를 설정했음
         * 다만 session.timeout.ms에 지정된 시간을 넘어가도록 컨슈머가 재실행 되지 않으면 리밸런싱 동작이 발생함
        * */
        props[GROUP_INSTANCE_ID_CONFIG] = "${InetAddress.getLocalHost().hostAddress}-${UUID.randomUUID()}"
        props[GROUP_ID_CONFIG] = groupId
        props[PARTITION_ASSIGNMENT_STRATEGY_CONFIG] = listOf(CooperativeStickyAssignor::class.java) // 협력적 스티키 파티션 할당 전략
        props[KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        return props
    }

    private fun reservationConfigConsumerProps(): Map<String, Any> {
        val props: MutableMap<String, Any> = mutableMapOf()
        props[BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        props[ENABLE_AUTO_COMMIT_CONFIG] = false // 수동 커밋
        props[AUTO_OFFSET_RESET_CONFIG] = autoOffsetReset
        props[SESSION_TIMEOUT_MS_CONFIG] = sessionTimeout
        props[HEARTBEAT_INTERVAL_MS_CONFIG] = heartbeatInterval
        props[MAX_POLL_INTERVAL_MS_CONFIG] = maxPollInterval
        props[MAX_POLL_RECORDS_CONFIG] = maxPollRecords
        props[ALLOW_AUTO_CREATE_TOPICS_CONFIG] = allowAutoCreateTopics
        /*
         * 스태틱 멤버십 -> 컨슈머 그룹 내에서 컨슈머가 재시작 등으로 그룹에서 나갔다가 다시 합류하더라도 리밸런싱이 일어나지 않도록 한다.
         * 스태틱 멤버십 적용을 위해 'GROUP_INSTANCE_ID' 를 설정했음
         * 다만 session.timeout.ms에 지정된 시간을 넘어가도록 컨슈머가 재실행 되지 않으면 리밸런싱 동작이 발생함
        * */
        props[GROUP_INSTANCE_ID_CONFIG] = "${InetAddress.getLocalHost().hostAddress}-${UUID.randomUUID()}"
        props[GROUP_ID_CONFIG] = groupId
        props[PARTITION_ASSIGNMENT_STRATEGY_CONFIG] = listOf(CooperativeStickyAssignor::class.java) // 협력적 스티키 파티션 할당 전략
        props[KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        return props
    }
}
