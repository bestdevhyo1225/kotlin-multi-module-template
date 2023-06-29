package kr.co.hyo.api.post.service

import kr.co.hyo.domain.member.dto.MemberFollowDto
import kr.co.hyo.domain.member.service.MemberFollowReadService
import kr.co.hyo.domain.member.service.MemberReadService
import kr.co.hyo.domain.post.dto.PostCreateDto
import kr.co.hyo.domain.post.dto.PostDto
import kr.co.hyo.domain.post.service.PostWriteService
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class PostFanoutService(
    private val postWriteService: PostWriteService,
    private val memberReadService: MemberReadService,
    private val memberFollowReadService: MemberFollowReadService,
) {

    private val kotlinLogger = KotlinLogging.logger {}

    fun createPost(dto: PostCreateDto): PostDto {
        val postDto: PostDto = postWriteService.create(dto = dto)
        if (memberReadService.isExceededFanoutMaxLimit(memberId = postDto.memberId)) {
            kotlinLogger.info { "팬아웃 하기위한 팔로우 회원수를 초과했습니다. (memberId: ${postDto.memberId})" }
            return postDto
        }
        // TODO: 코루틴 처리
        var lastFollowerId: Long = 0
        while (true) {
            val memberFollowDtos: List<MemberFollowDto> = memberFollowReadService.findFollowers(
                followingId = postDto.memberId,
                lastFollowerId = lastFollowerId,
            )

            if (memberFollowDtos.isEmpty()) {
                break
            }

            // TODO: Post 생성에 대한 Feed 메시지 발행

            lastFollowerId = memberFollowDtos.last().followerId
        }
        return postDto
    }
}
