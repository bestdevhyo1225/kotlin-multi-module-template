package kr.co.hyo

import kr.co.hyo.common.util.jwt.JwtParseHelper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import reactor.blockhound.BlockHound
import reactor.blockhound.integration.BlockHoundIntegration

@SpringBootApplication
class GatewayApplication

fun main(args: Array<String>) {
    // BlockHound
    BlockHound.install(
        BlockHoundIntegration { builder: BlockHound.Builder ->
            builder.allowBlockingCallsInside(JwtParseHelper::class.qualifiedName, "getClaims")
        }
    )

    // Run Application
    runApplication<GatewayApplication>(*args)
}
