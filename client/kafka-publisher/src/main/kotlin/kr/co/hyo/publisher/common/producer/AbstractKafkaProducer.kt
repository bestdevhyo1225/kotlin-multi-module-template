package kr.co.hyo.publisher.common.producer

import mu.KotlinLogging
import org.apache.kafka.common.KafkaException
import java.util.concurrent.ExecutionException

abstract class AbstractKafkaProducer {

    private val logger = KotlinLogging.logger {}

    protected fun execute(func: () -> Unit) {
        try {
            func()
        } catch (exception: InterruptedException) {
            logger.error { exception }
        } catch (exception: ExecutionException) {
            logger.error { exception }
        } catch (exception: KafkaException) {
            logger.error { exception }
        }
    }
}
