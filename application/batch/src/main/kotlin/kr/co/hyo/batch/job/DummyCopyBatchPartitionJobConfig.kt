package kr.co.hyo.batch.job

import jakarta.persistence.EntityManagerFactory
import kr.co.hyo.batch.listener.DummyCopyChunkListener
import kr.co.hyo.batch.partitioner.DummyIdRangePartitioner
import kr.co.hyo.domain.common.entity.Dummy
import kr.co.hyo.domain.common.entity.DummyCopy
import kr.co.hyo.domain.common.repository.DummyCopyJdbcRepository
import kr.co.hyo.domain.common.repository.DummyRepositorySupport
import mu.KotlinLogging
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class DummyCopyBatchPartitionJobConfig(
    @Value("\${dummy.chunk-size}")
    private val chunkSize: Int,
    @Value("\${task-executor.pool-size}")
    private val poolSize: Int,
    @Value("\${task-executor.keep-alive-seconds}")
    private val keepAliveSeconds: Int,
    private val jobRepository: JobRepository,
    private val platformTransactionManager: PlatformTransactionManager,
    private val entityManagerFactory: EntityManagerFactory,
    private val dummyRepositorySupport: DummyRepositorySupport,
    private val dummyCopyJdbcRepository: DummyCopyJdbcRepository,
) {

    private val logger = KotlinLogging.logger {}

    companion object {
        const val JOB = "dummyCopyBatchPartitionJob"
        private const val STEP = "${JOB}Step"
        private const val STEP_MANAGER = "${JOB}StepManager"
        private const val PARTITIONER = "${JOB}Partitioner"
        private const val TASK_EXECUTOR_PARTITION_HANDLER = "${JOB}TaskExecutorPartitionHandler"
        private const val TASK_EXECUTOR = "${JOB}TaskExecutor"
        private const val READER = "${JOB}Reader"
        private const val PROCESSOR = "${JOB}Processor"
        private const val WRITER = "${JOB}Writer"
        private const val LISTENER = "${JOB}Listener"
    }

    @Bean(JOB)
    fun job(): Job {
        logger.info { "####### job() #######" }
        return JobBuilder(JOB, jobRepository)
            .start(stepManager())
            .build()
    }

    @Bean(STEP_MANAGER)
    fun stepManager(): Step {
        logger.info("####### stepManager() #######")
        return StepBuilder(STEP_MANAGER, jobRepository)
            .partitioner(STEP, partitioner()) // Step에 사용될 Partition 구현체 등록
            .partitionHandler(taskExecutorPartitionHandler()) // 사용할 PartitionHandler 등록
            .step(step()) // 파티셔닝 될 Step 등록
            .build()
    }

    @Bean(STEP)
    fun step(): Step {
        logger.info { "####### step() #######" }
        return StepBuilder(STEP, jobRepository)
            .chunk<Dummy, DummyCopy>(chunkSize, platformTransactionManager)
            .reader(reader(minId = null, maxId = null))
            .processor(processor(minId = null, maxId = null))
            .writer(writer(minId = null, maxId = null))
            .listener(listener())
            .build()
    }

    @Bean(READER)
    @StepScope
    fun reader(
        @Value("#{stepExecutionContext[minId]}") minId: Long?,
        @Value("#{stepExecutionContext[maxId]}") maxId: Long?,
    ): JpaPagingItemReader<Dummy> {
        logger.info { "####### reader(minId: $minId, maxId: $maxId) #######" }
        val sql = "SELECT d FROM Dummy d WHERE d.id BETWEEN :minId AND :maxId"
        val params: Map<String, Any?> = mapOf("minId" to minId, "maxId" to maxId)
        return JpaPagingItemReaderBuilder<Dummy>()
            .name(READER)
            .entityManagerFactory(entityManagerFactory)
            .queryString(sql)
            .parameterValues(params)
            .pageSize(chunkSize)
            .build()
    }

    @Bean(PROCESSOR)
    @StepScope
    fun processor(
        @Value("#{stepExecutionContext[minId]}") minId: Long?,
        @Value("#{stepExecutionContext[maxId]}") maxId: Long?,
    ): ItemProcessor<Dummy, DummyCopy> {
        logger.info { "####### processor(minId: $minId, maxId: $maxId) #######" }
        return ItemProcessor {
            DummyCopy(dummyId = it.id!!, name = it.name)
        }
    }

    @Bean(WRITER)
    @StepScope
    fun writer(
        @Value("#{stepExecutionContext[minId]}") minId: Long?,
        @Value("#{stepExecutionContext[maxId]}") maxId: Long?,
    ): ItemWriter<DummyCopy> {
        logger.info { "####### writer(minId: $minId, maxId: $maxId) #######" }
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

    @Bean(PARTITIONER)
    @StepScope
    fun partitioner(): DummyIdRangePartitioner =
        DummyIdRangePartitioner(dummyRepositorySupport = dummyRepositorySupport)

    @Bean(TASK_EXECUTOR_PARTITION_HANDLER)
    fun taskExecutorPartitionHandler(): TaskExecutorPartitionHandler {
        logger.info { "####### taskExecutorPartitionHandler() #######" }
        // 멀티 스레드로 실행될 수 있도록 도와준다.
        val taskExecutorPartitionHandler = TaskExecutorPartitionHandler()
        taskExecutorPartitionHandler.gridSize = poolSize
        taskExecutorPartitionHandler.setTaskExecutor(taskExecutor())
        taskExecutorPartitionHandler.step = step()
        return taskExecutorPartitionHandler
    }

    @Bean(TASK_EXECUTOR)
    fun taskExecutor(): TaskExecutor {
        logger.info { "####### taskExecutor() #######" }
        val taskExecutor = ThreadPoolTaskExecutor()
        taskExecutor.corePoolSize = poolSize
        taskExecutor.maxPoolSize = poolSize
        taskExecutor.keepAliveSeconds = keepAliveSeconds
        taskExecutor.setThreadNamePrefix("partition-thread")
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true) // Queue 대기열 및 Task 가 완료된 이후에 Shutdown 여부
        taskExecutor.setAllowCoreThreadTimeOut(true) // KeepAliveSeconds 동안 Core Thread 가 작업을 받지 않으면, 종료한다.
        taskExecutor.initialize()
        return taskExecutor
    }
}
