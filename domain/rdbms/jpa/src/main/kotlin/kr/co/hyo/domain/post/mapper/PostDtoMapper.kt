package kr.co.hyo.domain.post.mapper

import kr.co.hyo.domain.post.dto.PostDto
import kr.co.hyo.domain.post.entity.Post

object PostDtoMapper {

    fun toDto(post: Post): PostDto =
        with(receiver = post) {
            PostDto(
                id = id!!,
                memberId = memberId,
                title = title,
                contents = contents,
                likeCount = likeCount,
                viewCount = viewCount,
                createdDate = createdDate,
                createdDatetime = createdDatetime,
            )
        }
}
