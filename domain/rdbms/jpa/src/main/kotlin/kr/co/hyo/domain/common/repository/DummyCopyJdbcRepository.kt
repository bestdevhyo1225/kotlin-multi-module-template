package kr.co.hyo.domain.common.repository

import kr.co.hyo.domain.common.entity.DummyCopy

interface DummyCopyJdbcRepository {
    fun saveAll(dummyCopies: List<DummyCopy>)
}
