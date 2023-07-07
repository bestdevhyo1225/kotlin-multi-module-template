package kr.co.hyo.config

import com.querydsl.jpa.JPQLTemplates.DEFAULT
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = ["kr.co.hyo.domain.*.entity"])
@EnableJpaRepositories(basePackages = ["kr.co.hyo.domain.*.repository"])
class JpaConfig {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @Bean
    fun jPAQueryFactory(): JPAQueryFactory = JPAQueryFactory(DEFAULT, entityManager)
}
