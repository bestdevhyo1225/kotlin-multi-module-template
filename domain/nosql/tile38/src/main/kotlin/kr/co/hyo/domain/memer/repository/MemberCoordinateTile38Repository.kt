package kr.co.hyo.domain.memer.repository

import io.lettuce.core.api.sync.RedisCommands
import io.lettuce.core.codec.StringCodec
import io.lettuce.core.output.NestedMultiOutput
import io.lettuce.core.output.StatusOutput
import io.lettuce.core.output.ValueOutput
import io.lettuce.core.protocol.CommandArgs
import kr.co.hyo.config.Tile38Commands.GET
import kr.co.hyo.config.Tile38Commands.NEARBY
import kr.co.hyo.config.Tile38Commands.SET
import mu.KotlinLogging
import org.springframework.stereotype.Repository

@Repository
class MemberCoordinateTile38Repository(
    private val tile38RedisCommands: RedisCommands<String, String>,
) {

    private val kotlinLogger = KotlinLogging.logger {}
    private val stringCodec = StringCodec.UTF8

    fun set(vararg args: Any) {
        val setResult: String = tile38RedisCommands.dispatch(
            SET.protocolKeyword,
            StatusOutput(stringCodec),
            CommandArgs(stringCodec).apply { args.forEach { add(it.toString()) } },
        )
        kotlinLogger.info { "set result: $setResult" }
    }

    fun get(vararg args: String): String {
        return tile38RedisCommands.dispatch(
            GET.protocolKeyword,
            ValueOutput(stringCodec),
            CommandArgs(stringCodec).apply { args.forEach { add(it) } },
        )
    }

    fun nearby(vararg args: Any): Map<String, Any> {
        val nearbyResult: List<Any> = tile38RedisCommands.dispatch(
            NEARBY.protocolKeyword,
            NestedMultiOutput(stringCodec),
            CommandArgs(stringCodec).apply { args.forEach { add(it.toString()) } },
        )
        return mapOf(
            "cursor" to nearbyResult[0].toString().toInt(),
            "items" to nearbyResult[1] as List<*>,
        )
    }
}
