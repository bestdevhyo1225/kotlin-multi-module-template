package kr.co.hyo.domain.member.mapper

import kr.co.hyo.domain.member.dto.MemberAuthDto
import kr.co.hyo.domain.member.entity.Member

object MemberAuthDtoMapper {

    fun toDto(member: Member): MemberAuthDto =
        with(receiver = member) {
            MemberAuthDto(id = id!!, jwtClaims = getJwtClaims())
        }
}
