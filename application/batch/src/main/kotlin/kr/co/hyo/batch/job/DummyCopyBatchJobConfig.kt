package kr.co.hyo.batch.job

import jakarta.persistence.EntityManagerFactory
import kr.co.hyo.batch.listener.DummyCopyChunkListener
import kr.co.hyo.domain.common.entity.Dummy
import kr.co.hyo.domain.common.entity.DummyCopy
import kr.co.hyo.domain.common.repository.DummyCopyJdbcRepository
import mu.KotlinLogging
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class DummyCopyBatchJobConfig(
    @Value("\${dummy.chunk-size:1000}")
    private val chunkSize: Int,
    private val jobRepository: JobRepository,
    private val platformTransactionManager: PlatformTransactionManager,
    private val entityManagerFactory: EntityManagerFactory,
    private val dummyCopyJdbcRepository: DummyCopyJdbcRepository,
) {

    private val logger = KotlinLogging.logger {}

    companion object {
        const val JOB = "dummyCopyBatchJob"
        private const val STEP = "${JOB}Step"
        private const val READER = "${JOB}Reader"
        private const val PROCESSOR = "${JOB}Processor"
        private const val WRITER = "${JOB}Writer"
        private const val LISTENER = "${JOB}Listener"
    }

    @Bean(JOB)
    fun job(): Job {
        logger.info { "####### job() #######" }
        return JobBuilder(JOB, jobRepository)
            .start(step())
            .build()
    }

    @Bean(STEP)
    fun step(): Step {
        logger.info { "####### step() #######" }
        return StepBuilder(STEP, jobRepository)
            .chunk<Dummy, DummyCopy>(chunkSize, platformTransactionManager)
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .listener(listener())
            .build()
    }

    @Bean(READER)
    @StepScope
    fun reader(): JpaPagingItemReader<Dummy> {
        logger.info { "####### reader() #######" }
        return JpaPagingItemReaderBuilder<Dummy>()
            .name(READER)
            .entityManagerFactory(entityManagerFactory)
            .queryString("SELECT d FROM Dummy d")
            .pageSize(chunkSize)
            .build()
    }

    @Bean(PROCESSOR)
    @StepScope
    fun processor(): ItemProcessor<Dummy, DummyCopy> {
        logger.info { "####### processor() #######" }
        return ItemProcessor {
            DummyCopy(dummyId = it.id!!, name = it.name)
        }
    }

    @Bean(WRITER)
    @StepScope
    fun writer(): ItemWriter<DummyCopy> {
        logger.info { "####### writer() #######" }
        return ItemWriter {
            dummyCopyJdbcRepository.saveAll(dummyCopies = it.toList())
        }
    }

    @Bean(LISTENER)
    @StepScope
    fun listener(): DummyCopyChunkListener {
        logger.info { "####### listener() #######" }
        return DummyCopyChunkListener()
    }
}
