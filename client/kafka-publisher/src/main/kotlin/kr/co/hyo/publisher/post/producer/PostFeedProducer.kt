package kr.co.hyo.publisher.post.producer

interface PostFeedProducer {
    fun <T : Any> send(event: T)
    fun <T : Any> sendAsync(event: T)
}
