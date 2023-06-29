rootProject.name = "kotlin-multi-module-template"

include(
    "application:api",
    "application:batch",
    "application:kafka-consumer",
    "client:kafka-publisher",
    "common",
    "domain:nosql:redis",
    "domain:rdbms:jpa",
)
