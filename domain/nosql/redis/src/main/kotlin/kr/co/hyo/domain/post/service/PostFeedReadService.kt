package kr.co.hyo.domain.post.service

import kr.co.hyo.common.util.page.PageRequestByPosition

interface PostFeedReadService {
    fun findPostIds(memberId: Long, pageRequest: PageRequestByPosition): List<Long>
}
