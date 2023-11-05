package kr.co.hyo.domain.chatting.repository

import kr.co.hyo.config.RedisEmbbededConfig
import kr.co.hyo.domain.chatting.entity.Chatting
import kr.co.hyo.domain.chatting.entity.ChattingRoom
import kr.co.hyo.domain.chatting.entity.ChattingRoomMember
import kr.co.hyo.domain.chatting.repository.redistemplate.ChattingRedisTemplateRepositoryImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import java.sql.Timestamp

@DataRedisTest(properties = ["spring.profiles.active=test"])
@DirtiesContext
@EnableAutoConfiguration
@ContextConfiguration(classes = [RedisEmbbededConfig::class, ChattingRedisTemplateRepositoryImpl::class])
@DisplayName("ChattingRedisTemplateRepository 단위 테스트")
class ChattingRedisTemplateRepositoryTests {

    @Autowired
    lateinit var chattingRedisTemplateRepository: ChattingRedisTemplateRepository

    @Autowired
    lateinit var redisTemplate: RedisTemplate<String, String>

    @AfterEach
    fun tearDown() {
        redisTemplate.delete(redisTemplate.keys("*"))
    }

    @Test
    fun `ChattingRoomId를 생성한다`() {
        // given
        val chattingRoomIdKey: String = ChattingRoom.getChattingRoomIdKey()

        // when
        val id1: Long = chattingRedisTemplateRepository.increment(key = chattingRoomIdKey)
        val id2: Long = chattingRedisTemplateRepository.increment(key = chattingRoomIdKey)
        val id3: Long = chattingRedisTemplateRepository.increment(key = chattingRoomIdKey)

        // then
        assertThat(id1).isEqualTo(1)
        assertThat(id2).isEqualTo(2)
        assertThat(id3).isEqualTo(3)
    }

    @Test
    fun `ChattingRoom을 생성한다`() {
        // given
        val chattingRoomIdKey: String = ChattingRoom.getChattingRoomIdKey()
        val id: Long = chattingRedisTemplateRepository.increment(key = chattingRoomIdKey)
        val chattingRoom = ChattingRoom(id = id, createdBy = 1, name = "저녁 식사 모임")

        // when
        chattingRedisTemplateRepository.set(key = chattingRoom.getChattingRoomKey(), value = chattingRoom)

        // then
        val findChattingRoom: ChattingRoom = chattingRedisTemplateRepository.get(
            key = chattingRoom.getChattingRoomKey(),
            clazz = ChattingRoom::class.java,
        )!!

        assertThat(findChattingRoom).isEqualTo(chattingRoom)
    }

    @Test
    fun `ChattingRoom 목록에 ChattingRoomId를 추가한다`() {
        // given
        val chattingRoomIdKey: String = ChattingRoom.getChattingRoomIdKey()
        val id: Long = chattingRedisTemplateRepository.increment(key = chattingRoomIdKey)
        val chattingRoom = ChattingRoom(id = id, createdBy = 1, name = "저녁 식사 모임")
        chattingRedisTemplateRepository.set(key = chattingRoom.getChattingRoomKey(), value = chattingRoom)

        // when
        chattingRedisTemplateRepository.zadd(
            key = chattingRoom.getChattingRoomListKey(),
            value = chattingRoom.id,
            score = Timestamp.valueOf(chattingRoom.createdAt).time.toDouble(),
        )

        // then
        val chattingRoomIds: List<Long> = chattingRedisTemplateRepository.zrevRange(
            key = chattingRoom.getChattingRoomListKey(),
            start = 0,
            end = 10,
            clazz = Long::class.java,
        )

        assertThat(chattingRoomIds).hasSize(1)
        assertThat(chattingRoomIds.first()).isEqualTo(1)
    }

    @Test
    fun `ChattingRoomMember를 등록한다`() {
        // given
        val chattingRoomIdKey: String = ChattingRoom.getChattingRoomIdKey()
        val id: Long = chattingRedisTemplateRepository.increment(key = chattingRoomIdKey)
        val chattingRoomMember = ChattingRoomMember(chattingRoomId = id, memberId = 1)

        // when
        chattingRedisTemplateRepository.sadd(
            key = chattingRoomMember.getChattingRoomMemberKey(),
            value = chattingRoomMember,
        )

        // then
        val count: Long = chattingRedisTemplateRepository.scard(key = chattingRoomMember.getChattingRoomMemberKey())

        assertThat(count).isEqualTo(1)
    }

    @Test
    fun `Chatting을 저장한다`() {
        // given
        val chattingRoomIdKey: String = ChattingRoom.getChattingRoomIdKey()
        val id: Long = chattingRedisTemplateRepository.increment(key = chattingRoomIdKey)
        val chatting = Chatting(chattingRoomId = id, memberId = 1, contents = "contents")

        // when
        chattingRedisTemplateRepository.lpush(key = chatting.getChattingKey(), value = chatting)

        // then
        val chattings: List<Chatting> = chattingRedisTemplateRepository.lrange(
            key = chatting.getChattingKey(),
            start = 0,
            end = 10,
            clazz = Chatting::class.java,
        )

        assertThat(chattings).hasSize(1)
        assertThat(chattings.first()).isEqualTo(chatting)
    }
}
