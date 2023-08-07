package kr.co.hyo.batch.tasklet

import kr.co.hyo.domain.common.repository.DummyJdbcRepository
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.batch.repeat.RepeatStatus.FINISHED
import org.springframework.batch.repeat.support.RepeatTemplate
import org.springframework.stereotype.Component

@Component
@StepScope
class DummyTasklet(
    private val repeatTemplate: RepeatTemplate,
    private val dummyJdbcRepository: DummyJdbcRepository,
    private val dummyTaskletRepeatCallback: DummyTaskletRepeatCallback,
) : Tasklet {

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
        dummyJdbcRepository.truncate()
        repeatTemplate.iterate(dummyTaskletRepeatCallback)
        return FINISHED
    }
}
