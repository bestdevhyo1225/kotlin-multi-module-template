package kr.co.hyo.domain.member.mapper

import kr.co.hyo.domain.member.dto.MemberCreateDto
import kr.co.hyo.domain.member.entity.Member

object MemberEntityMapper {

    fun toEntity(dto: MemberCreateDto): Member =
        with(receiver = dto) {
            Member(
                name = name,
                loginId = loginId,
                password = password,
                email = email,
            )
        }
}
