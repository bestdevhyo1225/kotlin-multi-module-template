package kr.co.hyo.domain.post.service.v1

import kr.co.hyo.domain.post.dto.PostCreateDto
import kr.co.hyo.domain.post.dto.PostDto
import kr.co.hyo.domain.post.entity.Post
import kr.co.hyo.domain.post.mapper.PostDtoMapper
import kr.co.hyo.domain.post.mapper.PostEntityMapper
import kr.co.hyo.domain.post.repository.PostRepository
import kr.co.hyo.domain.post.service.PostWriteService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PostWriteServiceV1(
    private val postRepository: PostRepository,
) : PostWriteService {

    override fun createPost(dto: PostCreateDto): PostDto {
        val post: Post = PostEntityMapper.toEntity(dto = dto)
        postRepository.save(post)
        return PostDtoMapper.toDto(post = post)
    }
}
