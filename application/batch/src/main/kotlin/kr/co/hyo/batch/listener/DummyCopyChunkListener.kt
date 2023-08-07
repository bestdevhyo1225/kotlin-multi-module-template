package kr.co.hyo.batch.listener

import mu.KotlinLogging
import org.springframework.batch.core.ChunkListener
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.scope.context.StepContext

open class DummyCopyChunkListener : ChunkListener {

    private val kotlinLogger = KotlinLogging.logger {}

    override fun beforeChunk(context: ChunkContext) {
        val stepContext: StepContext = context.stepContext
        val stepExecution: StepExecution = stepContext.stepExecution
        val jobExecution: JobExecution = stepExecution.jobExecution
        val jobParameters: JobParameters = jobExecution.jobParameters
        val version: String = jobParameters.parameters["version"]!!.value as String

        kotlinLogger.info { "####### [beforeChunk] jobVersion: $version #######" }
        kotlinLogger.info { "####### [beforeChunk] stepVersion: ${stepExecution.version} #######" }
    }

    override fun afterChunk(context: ChunkContext) {
        val stepContext: StepContext = context.stepContext
        val stepExecution: StepExecution = stepContext.stepExecution

        kotlinLogger.info { "####### [afterChunk] readCount: ${stepExecution.readCount} #######" }
        kotlinLogger.info { "####### [afterChunk] writeCount: ${stepExecution.writeCount} #######" }
        kotlinLogger.info { "####### [afterChunk] commitCount: ${stepExecution.commitCount} #######" }
    }

    override fun afterChunkError(context: ChunkContext) {
        val stepContext: StepContext = context.stepContext
        val stepExecution: StepExecution = stepContext.stepExecution

        kotlinLogger.info { "####### [afterChunkError] readCount: ${stepExecution.readCount} #######" }
        kotlinLogger.info { "####### [afterChunkError] writeCount: ${stepExecution.writeCount} #######" }
        kotlinLogger.info { "####### [afterChunkError] rollbackCount: ${stepExecution.rollbackCount} #######" }
    }
}
