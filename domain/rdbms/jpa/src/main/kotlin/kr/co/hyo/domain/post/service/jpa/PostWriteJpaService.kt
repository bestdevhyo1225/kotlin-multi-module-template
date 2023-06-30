package kr.co.hyo.domain.post.service.jpa

import kr.co.hyo.domain.post.dto.PostCreateDto
import kr.co.hyo.domain.post.dto.PostDto
import kr.co.hyo.domain.post.entity.Post
import kr.co.hyo.domain.post.mapper.PostDtoMapper
import kr.co.hyo.domain.post.mapper.PostEntityMapper
import kr.co.hyo.domain.post.repository.PostJpaRepository
import kr.co.hyo.domain.post.service.PostWriteService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PostWriteJpaService(
    private val postJpaRepository: PostJpaRepository,
) : PostWriteService {

    override fun create(dto: PostCreateDto): PostDto {
        val post: Post = PostEntityMapper.toEntity(dto = dto)
        postJpaRepository.save(post)
        return PostDtoMapper.toDto(post = post)
    }

    override fun incrementLikeCount(memberId: Long, postId: Long) {
        val post: Post = findPost(id = postId)
        post.incrementLikeCount(memberId = memberId)
    }

    override fun incrementViewCount(memberId: Long, postId: Long) {
        val post: Post = findPost(id = postId)
        post.incrementViewCount(memberId = memberId)
    }

    private fun findPost(id: Long): Post =
        postJpaRepository.findByIdOrNull(id = id) ?: throw NoSuchElementException("게시글이 존재하지 않습니다.")
}
