package kr.co.hyo.batch.job

import jakarta.persistence.EntityManagerFactory
import kr.co.hyo.batch.config.BatchConstants.SPRING_BATCH_JOB_NAME
import kr.co.hyo.domain.member.dto.MemberDto
import kr.co.hyo.domain.member.entity.Member
import kr.co.hyo.batch.listener.MemberChunkListener
import kr.co.hyo.batch.processor.MemberItemProcessor
import kr.co.hyo.batch.writer.MemberDtoItemWriter
import mu.KotlinLogging
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.batch.item.support.CompositeItemProcessor
import org.springframework.batch.item.support.CompositeItemWriter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class MemberBatchJobConfig(
    @Value("\${spring.batch.chunk-size:200}")
    private val chunkSize: Int,
    private val jobRepository: JobRepository,
    private val platformTransactionManager: PlatformTransactionManager,
    private val entityManagerFactory: EntityManagerFactory,
) {

    private val kotlinLogger = KotlinLogging.logger {}

    companion object {
        const val JOB = "memberBatchJob"
        private const val STEP = "${JOB}Step"
        private const val READER = "${JOB}Reader"
        private const val PROCESSOR = "${JOB}Processor"
        private const val WRITER = "${JOB}Writer"
        private const val LISTENER = "${JOB}Listener"
    }

    @Bean(JOB)
    @ConditionalOnProperty(value = [SPRING_BATCH_JOB_NAME], havingValue = JOB)
    fun job(): Job {
        kotlinLogger.info { "####### job() #######" }
        return JobBuilder(JOB, jobRepository)
            .start(step())
            .build()
    }

    @Bean(STEP)
    @ConditionalOnProperty(value = [SPRING_BATCH_JOB_NAME], havingValue = JOB)
    fun step(): Step {
        kotlinLogger.info { "####### step() #######" }
        return StepBuilder(STEP, jobRepository)
            .chunk<Member, MemberDto>(chunkSize, platformTransactionManager)
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .listener(listener())
            .build()
    }

    @Bean(READER)
    @ConditionalOnProperty(value = [SPRING_BATCH_JOB_NAME], havingValue = JOB)
    @StepScope
    fun reader(): JpaPagingItemReader<Member> {
        return JpaPagingItemReaderBuilder<Member>()
            .name(READER)
            .entityManagerFactory(entityManagerFactory)
            .queryString("""SELECT m FROM Member m WHERE m.deletedDateTime IS NULL""")
            .pageSize(chunkSize)
            .build()
    }

    @Bean(PROCESSOR)
    @ConditionalOnProperty(value = [SPRING_BATCH_JOB_NAME], havingValue = JOB)
    @StepScope
    fun processor(): CompositeItemProcessor<Member, MemberDto> {
        val compositeItemProcessor: CompositeItemProcessor<Member, MemberDto> = CompositeItemProcessor()
        val memberItemProcessors: List<MemberItemProcessor> = listOf(MemberItemProcessor())
        compositeItemProcessor.setDelegates(memberItemProcessors)
        return compositeItemProcessor
    }

    @Bean(WRITER)
    @ConditionalOnProperty(value = [SPRING_BATCH_JOB_NAME], havingValue = JOB)
    @StepScope
    fun writer(): CompositeItemWriter<MemberDto> {
        val compositeItemWriter: CompositeItemWriter<MemberDto> = CompositeItemWriter()
        val memberDtoItemWriters: List<ItemWriter<in MemberDto>> = listOf(MemberDtoItemWriter())
        compositeItemWriter.setDelegates(memberDtoItemWriters)
        return compositeItemWriter
    }

    @Bean(LISTENER)
    @ConditionalOnProperty(value = [SPRING_BATCH_JOB_NAME], havingValue = JOB)
    @StepScope
    fun listener(): MemberChunkListener {
        return MemberChunkListener()
    }
}
