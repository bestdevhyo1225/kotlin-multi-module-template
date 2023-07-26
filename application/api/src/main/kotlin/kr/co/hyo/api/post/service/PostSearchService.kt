package kr.co.hyo.api.post.service

import kr.co.hyo.common.util.page.PageByPosition
import kr.co.hyo.common.util.page.PageRequestByPosition
import kr.co.hyo.domain.post.dto.PostDto
import kr.co.hyo.domain.post.service.PostReadService
import org.springframework.stereotype.Service

@Service
class PostSearchService(
    private val postReadService: PostReadService,
) {

    fun search(keyword: String, pageRequestByPosition: PageRequestByPosition): PageByPosition<PostDto> {
        val (start: Long, size: Long) = pageRequestByPosition
        val posts: List<PostDto> = postReadService.findPosts(keyword = keyword, offset = start, limit = size)
        return PageByPosition(
            items = posts,
            nextPageRequestByPosition = pageRequestByPosition.next(itemSize = posts.size),
        )
    }
}
