package kr.co.hyo.job

import kr.co.hyo.config.BatchConstants.SPRING_BATCH_JOB_NAME
import kr.co.hyo.config.JpaConfig
import kr.co.hyo.job.MemberBatchJobConfig.Companion.JOB
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest

@SpringBatchTest
@SpringBootTest(
    properties = ["$SPRING_BATCH_JOB_NAME=$JOB"],
    classes = [
        JpaConfig::class,
        MemberBatchJobConfig::class,
    ]
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("MemberBatchJobConfig 통합 테스트")
class MemberBatchJobConfigTests {

    @Autowired
    lateinit var jobLauncherTestUtils: JobLauncherTestUtils

    @Test
    fun `회원 배치를 수행한다`() {
        // given
        val jobParameters = JobParametersBuilder()
            .addString("version", "20230624-001")
            .toJobParameters()

        // when
        val jobExecution = jobLauncherTestUtils.launchJob(jobParameters)

        // then
        assertThat(jobExecution.status).isEqualTo(BatchStatus.COMPLETED)
        assertThat(jobExecution.exitStatus).isEqualTo(ExitStatus.COMPLETED)
    }
}
