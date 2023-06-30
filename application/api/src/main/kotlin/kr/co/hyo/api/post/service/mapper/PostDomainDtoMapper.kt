package kr.co.hyo.api.post.service.mapper

import kr.co.hyo.domain.post.dto.PostCacheCreateDto
import kr.co.hyo.domain.post.dto.PostCacheDto
import kr.co.hyo.domain.post.dto.PostDto

object PostDomainDtoMapper {

    fun toPostCacheCreateDto(postDto: PostDto): PostCacheCreateDto =
        with(receiver = postDto) {
            PostCacheCreateDto(
                postId = id,
                memberId = memberId,
                title = title,
                contents = contents,
                createdDate = createdDate,
                createdDatetime = createdDatetime,
            )
        }

    fun toPostDto(postCacheDto: PostCacheDto, postLikeCount: Long, postViewCount: Long): PostDto =
        with(receiver = postCacheDto) {
            PostDto(
                id = postId,
                memberId = memberId,
                title = title,
                contents = contents,
                likeCount = postLikeCount,
                viewCount = postViewCount,
                createdDate = createdDate,
                createdDatetime = createdDatetime,
            )
        }
}
