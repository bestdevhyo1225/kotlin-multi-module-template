package kr.co.hyo.domain.post.service.redistemplate

import kr.co.hyo.common.util.page.PageRequestByPosition
import kr.co.hyo.domain.post.entity.PostFeed
import kr.co.hyo.domain.post.repository.PostFeedRedisTemplateRepository
import kr.co.hyo.domain.post.service.PostFeedReadService
import org.springframework.stereotype.Service

@Service
class PostFeedReadRedisTemplateService(
    private val postFeedRedisTemplateRepository: PostFeedRedisTemplateRepository,
) : PostFeedReadService {

    override fun findPostIds(memberId: Long, pageRequest: PageRequestByPosition): List<Long> {
        val postFeed = PostFeed(memberId = memberId)
        val (start: Long, size: Long) = pageRequest
        val end: Long = start.plus(size).minus(other = 1)
        return postFeedRedisTemplateRepository.zrevRange(
            key = postFeed.getMemberIdPostFeedsKey(),
            start = start,
            end = end,
            clazz = Long::class.java,
        )
    }
}
