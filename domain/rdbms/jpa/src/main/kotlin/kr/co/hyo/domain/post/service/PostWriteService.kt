package kr.co.hyo.domain.post.service

import kr.co.hyo.domain.post.dto.PostCreateDto
import kr.co.hyo.domain.post.dto.PostDto

interface PostWriteService {
    fun create(dto: PostCreateDto): PostDto
}
