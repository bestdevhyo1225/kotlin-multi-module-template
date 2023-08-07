package kr.co.hyo.batch.tasklet

import kr.co.hyo.domain.common.entity.Dummy
import kr.co.hyo.domain.common.repository.DummyJdbcRepository
import mu.KotlinLogging
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.repeat.RepeatCallback
import org.springframework.batch.repeat.RepeatContext
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.batch.repeat.RepeatStatus.CONTINUABLE
import org.springframework.batch.repeat.RepeatStatus.FINISHED
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@StepScope
class DummyTaskletRepeatCallback(
    @Value("\${dummy.tasklet.bulk-insert-total-size}")
    private val totalSize: Long,
    @Value("\${dummy.tasklet.bulk-insert-size}")
    private var bulkSize: Long,
    private val dummyJdbcRepository: DummyJdbcRepository,
) : RepeatCallback {

    private val logger = KotlinLogging.logger {}
    private var accumulatedBulkSize: Long = 0

    @Transactional
    override fun doInIteration(context: RepeatContext): RepeatStatus {
        if (accumulatedBulkSize == 0L && bulkSize > totalSize) {
            bulkSize = totalSize
        }
        if (accumulatedBulkSize >= totalSize) {
            logger.info { "dummy bulk insert finished (accumulatedBulkSize: $accumulatedBulkSize)" }
            return FINISHED
        }
        dummyJdbcRepository.saveAll(dummies = createDummies())
        accumulatedBulkSize += bulkSize
        logger.info { "dummy bulk insert completed (accumulatedBulkSize: $accumulatedBulkSize, totalSize: $totalSize)" }
        return CONTINUABLE
    }

    private fun createDummies(): List<Dummy> =
        (accumulatedBulkSize + 1..accumulatedBulkSize + bulkSize).map { Dummy(name = "name-${it}") }
}
