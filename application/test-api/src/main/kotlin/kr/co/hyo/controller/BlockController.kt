package kr.co.hyo.controller

import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class BlockController {

    private val logger = KotlinLogging.logger {}

    @GetMapping("/block/{requestId}")
    fun block(@PathVariable requestId: String): ResponseEntity<String> {
        logger.info { "request $requestId start" }
        Thread.sleep(5_000)
        logger.info { "request $requestId end" }
        return ResponseEntity.ok().body(requestId)
    }
}
