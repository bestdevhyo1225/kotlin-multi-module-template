package kr.co.hyo.api.post.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        val postDto: PostDto = postWriteService.createPost(dto = dto)
        postCacheWriteService.createPostCache(dto = PostDomainDtoMapper.toPostCacheCreateDto(postDto = postDto))
        if (memberReadService.isCanNotMemberFanoutMaxLimit(memberId = postDto.memberId)) {
            return postDto
        }
        CoroutineScope(context = Dispatchers.IO).launch { sendPostFeedEvent(postDto = postDto) }
        return postDto
    }

    private suspend fun sendPostFeedEvent(postDto: PostDto) {
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
    }
}
