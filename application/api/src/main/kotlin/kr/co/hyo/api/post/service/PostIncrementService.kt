package kr.co.hyo.api.post.service

import kr.co.hyo.domain.post.service.PostCacheWriteService
import kr.co.hyo.domain.post.service.PostWriteService
import org.springframework.stereotype.Service

@Service
class PostIncrementService(
    private val postCacheWriteService: PostCacheWriteService,
    private val postWriteService: PostWriteService,
) {

    fun incrementLikeCount(memberId: Long, postId: Long) {
        postWriteService.incrementLikeCount(memberId = memberId, postId = postId)
        postCacheWriteService.incrementLikeCount(postId = postId)
    }

    fun incrementViewCount(memberId: Long, postId: Long) {
        postWriteService.incrementViewCount(memberId = memberId, postId = postId)
        postCacheWriteService.incrementViewCount(postId = postId)
    }
}
