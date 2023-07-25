package kr.co.hyo.domain.post.repository

import kr.co.hyo.domain.post.entity.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long>
