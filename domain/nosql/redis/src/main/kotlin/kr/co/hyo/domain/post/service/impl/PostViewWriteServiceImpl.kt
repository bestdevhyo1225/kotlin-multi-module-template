package kr.co.hyo.domain.post.service.impl

import kr.co.hyo.domain.post.entity.PostView
import kr.co.hyo.domain.post.repository.PostRedisTemplateRepository
import kr.co.hyo.domain.post.service.PostViewWriteService
import org.springframework.stereotype.Service

@Service
class PostViewWriteServiceImpl(
    private val postRedisTemplateRepository: PostRedisTemplateRepository,
) : PostViewWriteService {

    override fun increment(postId: Long, postOwnMemberId: Long, memberId: Long): Long {
        val postView = PostView(
            postId = postId,
            postOwnMemberId = postOwnMemberId,
            memberId = memberId,
        )
        val key: String = postView.getPostViewCountKey()
        return if (postView.isOwn()) {
            postRedisTemplateRepository.get(key = key, clazz = Long::class.java) ?: 0L
        } else {
            postRedisTemplateRepository.increment(key = key)
        }
    }
}
