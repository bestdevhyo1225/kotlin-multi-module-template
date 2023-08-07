package kr.co.hyo.domain.common.repository.impl

import kr.co.hyo.domain.common.entity.Dummy
import kr.co.hyo.domain.common.repository.DummyJdbcRepository
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.PreparedStatement

@Repository
@Transactional(readOnly = true)
class DummyJdbcRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate,
) : DummyJdbcRepository {

    @Transactional
    override fun saveAll(dummies: List<Dummy>) {
        val bulkInsertQuery = "INSERT INTO `dummy` (`name`) VALUES (?)"
        jdbcTemplate.batchUpdate(bulkInsertQuery, object : BatchPreparedStatementSetter {
            override fun setValues(preparedStatement: PreparedStatement, index: Int) {
                preparedStatement.setString(1, dummies[index].name)
            }

            override fun getBatchSize(): Int {
                return dummies.size
            }
        })
    }

    override fun truncate() {
        jdbcTemplate.execute("TRUNCATE TABLE `dummy`")
    }
}
