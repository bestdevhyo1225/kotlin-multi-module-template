package kr.co.hyo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NettyTcpServerApplication

fun main(args: Array<String>) {
    runApplication<NettyTcpServerApplication>(*args)
}
