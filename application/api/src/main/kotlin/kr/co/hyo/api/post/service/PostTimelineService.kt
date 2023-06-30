package kr.co.hyo.api.post.service

import kr.co.hyo.common.util.page.PageByPosition
import kr.co.hyo.common.util.page.PageRequestByPosition
import kr.co.hyo.domain.member.dto.MemberDto
import kr.co.hyo.domain.member.dto.MemberFollowDto
import kr.co.hyo.domain.member.service.MemberFollowReadService
import kr.co.hyo.domain.member.service.MemberReadService
import kr.co.hyo.domain.member.service.MemberWriteService
import kr.co.hyo.domain.post.dto.PostDto
import kr.co.hyo.domain.post.service.PostFeedReadService
import kr.co.hyo.domain.post.service.PostFeedWriteService
import kr.co.hyo.domain.post.service.PostReadService
import org.springframework.stereotype.Service

@Service
class PostTimelineService(
    private val postFeedReadService: PostFeedReadService,
    private val postFeedWriteService: PostFeedWriteService,
    private val postReadService: PostReadService,
    private val memberFollowReadService: MemberFollowReadService,
    private val memberReadService: MemberReadService,
    private val memberWriteService: MemberWriteService,
) {

    fun refreshPosts(memberId: Long) {
        val memberFollowDtos: List<MemberFollowDto> = memberFollowReadService.findFollowings(followerId = memberId)
        val memberDto: MemberDto = memberReadService.find(memberId = memberId)
        val postDtos: List<PostDto> = postReadService.findPosts(
            memberIds = memberFollowDtos.map { it.followingId },
            timelineUpdatedDatetime = memberDto.timelineUpdatedDatetime,
        )
        postDtos.forEach { postFeedWriteService.create(memberId = memberId, postId = it.id) }
        memberWriteService.changeTimelineUpdatedDatetime(memberId = memberId)
    }

    fun findPosts(memberId: Long, pageRequestByPosition: PageRequestByPosition): PageByPosition<PostDto> {
        val (start: Long, size: Long) = pageRequestByPosition
        val end: Long = start.plus(other = size).minus(other = 1)
        val postIds: List<Long> = postFeedReadService.findPostIds(memberId = memberId, start = start, end = end)
        val postDtos: List<PostDto> = postReadService.findPosts(postIds = postIds)
        return PageByPosition(
            items = postDtos,
            nextPageRequestByPosition = pageRequestByPosition.next(itemSize = postIds.size)
        )
    }

    fun findPost(id: Long): PostDto {
        return postReadService.findPost(id = id)
    }
}
