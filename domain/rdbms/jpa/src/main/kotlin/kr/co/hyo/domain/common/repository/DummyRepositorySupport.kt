package kr.co.hyo.domain.common.repository

interface DummyRepositorySupport {
    fun findMinId(): Long
    fun findMaxId(): Long
}
