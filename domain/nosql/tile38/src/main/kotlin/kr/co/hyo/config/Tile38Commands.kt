package kr.co.hyo.config

import io.lettuce.core.protocol.ProtocolKeyword
import java.nio.charset.StandardCharsets

enum class Tile38Commands(val protocolKeyword: ProtocolKeyword) {
    SET(generateKeyword(name = "SET")),
    GET(generateKeyword(name = "GET")),
    NEARBY(generateKeyword(name = "NEARBY")),
    ;
}

private fun generateKeyword(name: String) = object : ProtocolKeyword {
    val byteArray = name.toByteArray(StandardCharsets.US_ASCII)

    override fun getBytes(): ByteArray {
        return byteArray
    }

    override fun name(): String {
        return name
    }
}
