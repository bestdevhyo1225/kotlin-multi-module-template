package kr.co.hyo.batch.job

import kr.co.hyo.batch.tasklet.DummyTasklet
import mu.KotlinLogging
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class DummyBatchJobConfig(
    private val jobRepository: JobRepository,
    private val platformTransactionManager: PlatformTransactionManager,
    private val dummyTasklet: DummyTasklet,
) {

    private val logger = KotlinLogging.logger {}

    companion object {
        const val JOB = "dummyBatchJob"
        private const val STEP = "${JOB}Step"
        private const val TASKLET = "${JOB}Tasklet"
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
            .tasklet(dummyTasklet, platformTransactionManager)
            .build()
    }
}
