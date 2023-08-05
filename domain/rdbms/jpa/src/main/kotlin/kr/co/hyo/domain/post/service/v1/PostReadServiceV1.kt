package kr.co.hyo.domain.post.service.v1

import kr.co.hyo.domain.post.dto.PostDto
import kr.co.hyo.domain.post.entity.Post
import kr.co.hyo.domain.post.mapper.PostDtoMapper
import kr.co.hyo.domain.post.repository.PostRepositorySupport
import kr.co.hyo.domain.post.service.PostReadService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class PostReadServiceV1(
    private val postRepositorySupport: PostRepositorySupport,
) : PostReadService {

    override fun findPost(postId: Long): PostDto {
        val post: Post = postRepositorySupport.find(id = postId)
        return PostDtoMapper.toDto(post = post)
    }

    override fun findPosts(postIds: List<Long>): List<PostDto> {
        if (postIds.isEmpty()) {
            return emptyList()
        }
        val posts: List<Post> = postRepositorySupport.findAll(ids = postIds)
        return posts.map { PostDtoMapper.toDto(post = it) }
    }

    override fun findPosts(keyword: String, offset: Long, limit: Long): List<PostDto> {
        val posts: List<Post> = postRepositorySupport.findAll(keyword = keyword, offset = offset, limit = limit)
        return posts.map { PostDtoMapper.toDto(post = it) }
    }

    override fun findPostIds(
        memberIds: List<Long>,
        timelineUpdatedDatetime: LocalDateTime?,
        postLastId: Long,
        limit: Long,
    ): List<Long> {
        if (memberIds.isEmpty()) {
            return emptyList()
        }
        return postRepositorySupport.findIds(
            memberIds = memberIds,
            timelineUpdatedDatetime = timelineUpdatedDatetime,
            lastId = postLastId,
            limit = limit,
        )
    }
}
