package kr.co.hyo.consumer.post.request

data class PostFeedRequest(
    val followerId: Long,
    val postId: Long,
)
