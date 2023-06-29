package kr.co.hyo.consumer.post.listener.request

data class PostFeedRequest(
    val followerId: Long,
    val postId: Long,
)
