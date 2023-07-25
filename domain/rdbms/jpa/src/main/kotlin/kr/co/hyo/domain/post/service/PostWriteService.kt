package kr.co.hyo.domain.post.service

import kr.co.hyo.domain.post.dto.PostCreateDto
import kr.co.hyo.domain.post.dto.PostDto

interface PostWriteService {
    fun createPost(dto: PostCreateDto): PostDto
}
