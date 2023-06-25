rootProject.name = "kotlin-multi-module-template"

include(
    "application:api",
    "application:batch",
    "common",
    "domain:nosql:redis",
    "domain:nosql:tile38",
    "domain:rdbms:jpa",
)
