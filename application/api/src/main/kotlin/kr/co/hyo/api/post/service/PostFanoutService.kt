package kr.co.hyo.api.post.service

import kr.co.hyo.api.post.mapper.PostDomainDtoMapper
import kr.co.hyo.domain.member.dto.MemberFollowDto
import kr.co.hyo.domain.member.service.MemberFollowReadService
import kr.co.hyo.domain.member.service.MemberReadService
import kr.co.hyo.domain.post.dto.PostCreateDto
import kr.co.hyo.domain.post.dto.PostDto
import kr.co.hyo.domain.post.service.PostCacheWriteService
import kr.co.hyo.domain.post.service.PostFeedWriteService
import kr.co.hyo.domain.post.service.PostWriteService
import org.springframework.stereotype.Service

@Service
class PostFanoutService(
    private val postCacheWriteService: PostCacheWriteService,
    private val postWriteService: PostWriteService,
    private val postFeedWriteService: PostFeedWriteService,
    private val memberReadService: MemberReadService,
    private val memberFollowReadService: MemberFollowReadService,
) {

    fun createPost(dto: PostCreateDto): PostDto {
        val postDto: PostDto = postWriteService.create(dto = dto)
        postCacheWriteService.create(dto = PostDomainDtoMapper.toPostCacheCreateDto(postDto = postDto))
        if (memberReadService.isCanNotFanoutMaxLimit(memberId = postDto.memberId)) {
            return postDto
        }
        var lastFollowerId: Long = 0
        while (true) {
            val memberFollowDtos: List<MemberFollowDto> =
                memberFollowReadService.findFollowers(followingId = postDto.memberId, lastFollowerId = lastFollowerId)
            if (memberFollowDtos.isEmpty()) {
                break
            }
            memberFollowDtos.forEach {
                postFeedWriteService.create(memberId = it.followerId, postId = postDto.id)
            }
            lastFollowerId = memberFollowDtos.last().followerId
        }
        return postDto
    }
}
