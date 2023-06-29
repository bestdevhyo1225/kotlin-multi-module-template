package kr.co.hyo.domain.post.mapper

import kr.co.hyo.domain.post.dto.PostCreateDto
import kr.co.hyo.domain.post.entity.Post

object PostEntityMapper {

    fun toEntity(dto: PostCreateDto): Post =
        with(receiver = dto) {
            Post(
                memberId = memberId,
                title = title,
                contents = contents,
            )
        }
}
