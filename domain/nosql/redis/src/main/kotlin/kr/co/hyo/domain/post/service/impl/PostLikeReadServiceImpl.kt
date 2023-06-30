package kr.co.hyo.domain.post.service.impl

import kr.co.hyo.domain.post.entity.PostLike
import kr.co.hyo.domain.post.repository.PostRedisTemplateRepository
import kr.co.hyo.domain.post.service.PostLikeReadService
import org.springframework.stereotype.Service

@Service
class PostLikeReadServiceImpl(
    private val postRedisTemplateRepository: PostRedisTemplateRepository,
) : PostLikeReadService {

    override fun count(postId: Long): Long {
        val key: String = PostLike(postId = postId).getPostLikeMembersKey()
        return postRedisTemplateRepository.scard(key = key)
    }
}
