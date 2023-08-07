package kr.co.hyo.domain.common.repository

import kr.co.hyo.config.JpaConfig
import kr.co.hyo.domain.common.entity.Dummy
import kr.co.hyo.domain.common.repository.impl.DummyJdbcRepositoryImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@ContextConfiguration(classes = [JpaConfig::class, DummyJdbcRepositoryImpl::class])
@DisplayName("DummyJdbcRepository 단위 테스트")
class DummyJdbcRepositoryTests {

    @Autowired
    lateinit var dummyJdbcRepository: DummyJdbcRepository

    @Autowired
    lateinit var dummyRepository: DummyRepository

    @Test
    fun `Dummy 데이터를 대량으로 등록한다`() {
        // given
        val dummies: List<Dummy> = (1..100).map { Dummy(name = "name-${it}") }

        // when
        dummyJdbcRepository.saveAll(dummies = dummies)

        // then
        assertThat(dummyRepository.findAll()).hasSize(dummies.size)
    }
}
