import org.springframework.boot.gradle.tasks.bundling.BootJar

// 메인 클래스들과 디펜던시를 포함해 실행 가능한 jar를 만든다.
tasks.getByName<BootJar>("bootJar") {
    enabled = true
}

dependencies {
    implementation(project(":common"))
    implementation(project(":domain:nosql:redis"))
    implementation(project(":domain:nosql:tile38"))
    implementation(project(":domain:rdbms:jpa"))

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0") {
        exclude("org.yaml", "snakeyaml")
    }
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")

    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    testImplementation("org.springframework.security:spring-security-test")
}
