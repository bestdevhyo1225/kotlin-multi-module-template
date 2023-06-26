package kr.co.hyo.batch.writer

import kr.co.hyo.domain.member.dto.MemberDto
import mu.KotlinLogging
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter

class MemberDtoItemWriter : ItemWriter<MemberDto> {

    private val kotlinLogger = KotlinLogging.logger {}

    override fun write(chunk: Chunk<out MemberDto>) {
        chunk.forEach {
            kotlinLogger.info { "####### [write] memberDto.id: ${it.id} #######" }
        }
    }
}
