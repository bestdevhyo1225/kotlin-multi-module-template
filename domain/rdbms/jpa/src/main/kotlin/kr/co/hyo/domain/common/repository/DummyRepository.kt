package kr.co.hyo.domain.common.repository

import kr.co.hyo.domain.common.entity.Dummy
import org.springframework.data.jpa.repository.JpaRepository

interface DummyRepository : JpaRepository<Dummy, Long>
