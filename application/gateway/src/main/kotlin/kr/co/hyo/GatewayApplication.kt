package kr.co.hyo

import kr.co.hyo.common.util.jwt.JwtParseHelper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import reactor.blockhound.BlockHound
import reactor.blockhound.integration.BlockHoundIntegration

@SpringBootApplication
class GatewayApplication

fun main(args: Array<String>) {
    // BlockHound를 실행하려면, '-XX:+AllowRedefinitionToAddDeleteMethods' 을 설정해서 실행해야 에러가 발생하지 않는다.
    BlockHound.install(
        BlockHoundIntegration { builder: BlockHound.Builder ->
            builder.allowBlockingCallsInside(JwtParseHelper::class.qualifiedName, "getClaims")
        }
    )

//    // Event Loop Thread 1개만 사용
//    System.setProperty("reactor.netty.ioWorkerCount", "1")

    // Run Application
    runApplication<GatewayApplication>(*args)
}
