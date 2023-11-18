rootProject.name = "kotlin-multi-module-template"

include(
    "application:api",
    "application:batch",
    "application:gateway",
    "application:kafka-consumer",
    "application:netty-tcp-server",
    "application:tcp-client",
    "application:web-socket",
    "client:kafka-publisher",
    "common",
    "domain:nosql:redis",
    "domain:rdbms:jpa",
)
