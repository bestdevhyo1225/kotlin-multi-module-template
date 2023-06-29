package kr.co.hyo.domain.post.service.jpa

import kr.co.hyo.domain.post.dto.PostDto
import kr.co.hyo.domain.post.entity.Post
import kr.co.hyo.domain.post.mapper.PostDtoMapper
import kr.co.hyo.domain.post.repository.PostJpaRepositorySupport
import kr.co.hyo.domain.post.service.PostReadService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostReadJpaService(
    private val postJpaRepositorySupport: PostJpaRepositorySupport,
) : PostReadService {

    override fun find(memberId: Long, id: Long): PostDto {
        val post: Post = postJpaRepositorySupport.findByMemberIdAndId(memberId = memberId, id = id)
        return PostDtoMapper.toDto(post = post)
    }
}
