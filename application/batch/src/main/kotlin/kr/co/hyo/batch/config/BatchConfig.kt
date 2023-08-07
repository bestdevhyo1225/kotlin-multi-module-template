package kr.co.hyo.batch.config

import org.springframework.batch.repeat.support.RepeatTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BatchConfig {

    @Bean
    fun repeatTemplate(): RepeatTemplate {
        return RepeatTemplate()
    }
}
