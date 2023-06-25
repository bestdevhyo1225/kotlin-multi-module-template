package kr.co.hyo.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "tile38")
data class Tile38Properties @ConstructorBinding constructor(
    val host: String,
    val port: Int,
    val pool: Tile38PoolProperties,
)

data class Tile38PoolProperties @ConstructorBinding constructor(
    val minIdle: Int,
    val maxIdle: Int,
    val maxTotal: Int,
)
