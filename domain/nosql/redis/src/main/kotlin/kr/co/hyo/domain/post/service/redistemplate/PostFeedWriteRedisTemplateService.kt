package kr.co.hyo.domain.post.service.redistemplate

import kr.co.hyo.domain.post.entity.PostFeed
import kr.co.hyo.domain.post.entity.PostFeed.Companion.ZSET_POST_FEED_MAX_LIMIT
import kr.co.hyo.domain.post.repository.PostRedisTemplateRepository
import kr.co.hyo.domain.post.service.PostFeedWriteService
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDateTime

@Service
class PostFeedWriteRedisTemplateService(
    private val postRedisTemplateRepository: PostRedisTemplateRepository,
) : PostFeedWriteService {

    private val kotlinLogger = KotlinLogging.logger {}

    override fun create(memberId: Long, postId: Long) {
        val postFeed = PostFeed(memberId = memberId)
        val key: String = postFeed.getMemberIdPostFeedsKey()
        val score: Double = Timestamp.valueOf(LocalDateTime.now()).time.toDouble()
        kotlinLogger.info { "key: $key, postId:$postId, score: $score" }
        postRedisTemplateRepository.zadd(key = key, value = postId, score = score)
        postRedisTemplateRepository.zremRangeByRank(
            key = key,
            start = ZSET_POST_FEED_MAX_LIMIT,
            end = ZSET_POST_FEED_MAX_LIMIT
        )
    }
}
