package kr.co.hyo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import reactor.blockhound.BlockHound

@SpringBootApplication
class GatewayApplication

fun main(args: Array<String>) {
    // BlockHound
    BlockHound.install()

    // Run Application
    runApplication<GatewayApplication>(*args)
}
