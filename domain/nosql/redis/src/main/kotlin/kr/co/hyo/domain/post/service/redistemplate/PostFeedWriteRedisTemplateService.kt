package kr.co.hyo.domain.post.service.redistemplate

import kr.co.hyo.domain.post.entity.PostFeed
import kr.co.hyo.domain.post.entity.PostFeed.Companion.ZSET_POST_FEED_MAX_LIMIT
import kr.co.hyo.domain.post.repository.PostFeedRedisTemplateRepository
import kr.co.hyo.domain.post.service.PostFeedWriteService
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDateTime

@Service
class PostFeedWriteRedisTemplateService(
    private val postFeedRedisTemplateRepository: PostFeedRedisTemplateRepository,
) : PostFeedWriteService {

    override fun create(memberId: Long, postId: Long) {
        val postFeed = PostFeed(memberId = memberId)
        val key: String = postFeed.getMemberIdFeedsKey()
        val score: Double = Timestamp.valueOf(LocalDateTime.now()).time.toDouble()
        postFeedRedisTemplateRepository.zadd(key = key, value = postId, score = score)
        postFeedRedisTemplateRepository.zremRangeByRank(
            key = key,
            start = ZSET_POST_FEED_MAX_LIMIT,
            end = ZSET_POST_FEED_MAX_LIMIT
        )
    }
}
