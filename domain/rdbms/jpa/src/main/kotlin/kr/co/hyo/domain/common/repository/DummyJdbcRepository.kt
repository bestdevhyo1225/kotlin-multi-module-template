package kr.co.hyo.domain.common.repository

import kr.co.hyo.domain.common.entity.Dummy

interface DummyJdbcRepository {
    fun saveAll(dummies: List<Dummy>)
    fun truncate()
}
