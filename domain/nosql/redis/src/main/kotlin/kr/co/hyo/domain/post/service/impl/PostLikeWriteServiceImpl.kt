package kr.co.hyo.domain.post.service.impl

import kr.co.hyo.domain.post.entity.PostLike
import kr.co.hyo.domain.post.repository.PostRedisTemplateRepository
import kr.co.hyo.domain.post.service.PostLikeWriteService
import org.springframework.stereotype.Service

@Service
class PostLikeWriteServiceImpl(
    private val postRedisTemplateRepository: PostRedisTemplateRepository,
) : PostLikeWriteService {

    override fun likePost(postId: Long, memberId: Long) {
        val key: String = PostLike(postId = postId).getPostLikeMembersKey()
        postRedisTemplateRepository.sadd(key = key, value = memberId)
    }

    override fun deleteLikePost(postId: Long, memberId: Long) {
        val key: String = PostLike(postId = postId).getPostLikeMembersKey()
        postRedisTemplateRepository.srem(key = key, value = memberId)
    }
}
