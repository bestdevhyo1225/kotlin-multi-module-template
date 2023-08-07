package kr.co.hyo.domain.common.repository.impl

import kr.co.hyo.domain.common.entity.DummyCopy
import kr.co.hyo.domain.common.repository.DummyCopyJdbcRepository
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.PreparedStatement

@Repository
@Transactional(readOnly = true)
class DummyCopyJdbcRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate,
) : DummyCopyJdbcRepository {

    override fun saveAll(dummyCopies: List<DummyCopy>) {
        val bulkInsertQuery = "INSERT INTO `dummy_copy` (`dummy_id`, `name`) VALUES (?, ?)"
        jdbcTemplate.batchUpdate(bulkInsertQuery, object : BatchPreparedStatementSetter {
            override fun setValues(preparedStatement: PreparedStatement, index: Int) {
                preparedStatement.setLong(1, dummyCopies[index].dummyId)
                preparedStatement.setString(2, dummyCopies[index].name)
            }

            override fun getBatchSize(): Int {
                return dummyCopies.size
            }
        })
    }
}
