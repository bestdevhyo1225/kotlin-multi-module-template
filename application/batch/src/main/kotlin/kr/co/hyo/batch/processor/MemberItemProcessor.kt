package kr.co.hyo.batch.processor

import kr.co.hyo.domain.member.dto.MemberDto
import kr.co.hyo.domain.member.entity.Member
import kr.co.hyo.domain.member.mapper.MemberDtoMapper
import mu.KotlinLogging
import org.springframework.batch.item.ItemProcessor

class MemberItemProcessor : ItemProcessor<Member, MemberDto> {

    private val kotlinLogger = KotlinLogging.logger {}

    override fun process(item: Member): MemberDto {
        kotlinLogger.info {"####### [process] member.id: ${item.id} #######" }
        return MemberDtoMapper.toDto(member = item)
    }
}
