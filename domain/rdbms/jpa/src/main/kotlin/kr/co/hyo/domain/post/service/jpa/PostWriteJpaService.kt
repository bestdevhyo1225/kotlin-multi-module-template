package kr.co.hyo.domain.post.service.jpa

import kr.co.hyo.domain.post.dto.PostCreateDto
import kr.co.hyo.domain.post.dto.PostDto
import kr.co.hyo.domain.post.entity.Post
import kr.co.hyo.domain.post.mapper.PostDtoMapper
import kr.co.hyo.domain.post.mapper.PostEntityMapper
import kr.co.hyo.domain.post.repository.PostJpaRepository
import kr.co.hyo.domain.post.service.PostWriteService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PostWriteJpaService(
    private val postJpaRepository: PostJpaRepository,
) : PostWriteService {

    override fun createPost(dto: PostCreateDto): PostDto {
        val post: Post = PostEntityMapper.toEntity(dto = dto)
        postJpaRepository.save(post)
        return PostDtoMapper.toDto(post = post)
    }
}
