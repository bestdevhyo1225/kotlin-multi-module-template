package kr.co.hyo.domain.post.service.jpa

import kr.co.hyo.domain.post.dto.PostDto
import kr.co.hyo.domain.post.entity.Post
import kr.co.hyo.domain.post.mapper.PostDtoMapper
import kr.co.hyo.domain.post.repository.PostJpaRepositorySupport
import kr.co.hyo.domain.post.service.PostReadService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class PostReadJpaService(
    private val postJpaRepositorySupport: PostJpaRepositorySupport,
) : PostReadService {

    override fun findPost(postId: Long): PostDto {
        val post: Post = postJpaRepositorySupport.findById(id = postId)
        return PostDtoMapper.toDto(post = post)
    }

    override fun findPosts(postIds: List<Long>): List<PostDto> {
        if (postIds.isEmpty()) {
            return emptyList()
        }
        val posts: List<Post> = postJpaRepositorySupport.findAllByIds(ids = postIds)
        return posts.map { PostDtoMapper.toDto(post = it) }
    }

    override fun findPostIds(
        memberIds: List<Long>,
        timelineUpdatedDatetime: LocalDateTime?,
        lastPostId: Long,
        limit: Long,
    ): List<Long> {
        if (memberIds.isEmpty()) {
            return emptyList()
        }
        return postJpaRepositorySupport.findIds(
            memberIds = memberIds,
            timelineUpdatedDatetime = timelineUpdatedDatetime,
            lastId = lastPostId,
            limit = limit,
        )
    }
}
