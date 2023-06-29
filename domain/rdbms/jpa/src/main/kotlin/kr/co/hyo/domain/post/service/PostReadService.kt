package kr.co.hyo.domain.post.service

import kr.co.hyo.domain.post.dto.PostDto

interface PostReadService {
    fun find(memberId: Long, id: Long): PostDto
}
