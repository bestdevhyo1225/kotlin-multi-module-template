package kr.co.hyo.api.post.service

import kr.co.hyo.domain.member.dto.MemberFollowDto
import kr.co.hyo.domain.member.service.MemberFollowReadService
import kr.co.hyo.domain.member.service.MemberReadService
import kr.co.hyo.domain.post.dto.PostCreateDto
import kr.co.hyo.domain.post.dto.PostDto
import kr.co.hyo.domain.post.service.PostFeedWriteService
import kr.co.hyo.domain.post.service.PostWriteService
import kr.co.hyo.publisher.post.producer.PostFeedProducer
import org.springframework.stereotype.Service

@Service
class PostFanoutService(
    private val postWriteService: PostWriteService,
    private val postFeedProducer: PostFeedProducer,
    private val postFeedWriteService: PostFeedWriteService,
    private val memberReadService: MemberReadService,
    private val memberFollowReadService: MemberFollowReadService,
) {

    fun createPost(dto: PostCreateDto): PostDto {
        val postDto: PostDto = postWriteService.create(dto = dto)
        if (memberReadService.isCanNotFanoutMaxLimit(memberId = postDto.memberId)) {
            return postDto
        }

        // TODO: 코루틴 처리
        var lastFollowerId: Long = 0
        while (true) {
            val memberFollowDtos: List<MemberFollowDto> =
                memberFollowReadService.findFollowers(followingId = postDto.memberId, lastFollowerId = lastFollowerId)

            if (memberFollowDtos.isEmpty()) {
                break
            }

            memberFollowDtos.forEach {
//                val postFeedDto = PostFeedDto(followerId = it.followerId, postId = postDto.id)
//                postFeedProducer.sendAsync(event = postFeedDto)
                postFeedWriteService.create(memberId = it.followerId, postId = postDto.id)
            }

            lastFollowerId = memberFollowDtos.last().followerId
        }

        return postDto
    }
}
