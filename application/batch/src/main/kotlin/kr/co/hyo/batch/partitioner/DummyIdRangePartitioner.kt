package kr.co.hyo.batch.partitioner

import kr.co.hyo.domain.common.repository.DummyRepositorySupport
import org.springframework.batch.core.partition.support.Partitioner
import org.springframework.batch.item.ExecutionContext

open class DummyIdRangePartitioner(
    private val dummyRepositorySupport: DummyRepositorySupport,
) : Partitioner {

    override fun partition(gridSize: Int): MutableMap<String, ExecutionContext> {
        val minId: Long = dummyRepositorySupport.findMinId()
        val maxId: Long = dummyRepositorySupport.findMaxId()
        val targetSize: Long = (maxId - minId) / gridSize + 1
        var number: Long = 0
        var start: Long = minId
        var end = start + targetSize - 1
        val result: MutableMap<String, ExecutionContext> = HashMap()
        while (start <= maxId) {
            val executionContext = ExecutionContext()
            result["Partition-$number"] = executionContext
            if (end >= maxId) {
                end = maxId
            }
            executionContext.putLong("minId", start) // 각 파티션마다 사용될 minId
            executionContext.putLong("maxId", end) // 각 파티션마다 사용될 maxId
            start += targetSize
            end += targetSize
            number++
        }
        return result
    }
}
