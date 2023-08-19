import org.springframework.boot.gradle.tasks.bundling.BootJar

// 메인 클래스들과 디펜던시를 포함해 실행 가능한 jar를 만든다.
tasks.getByName<BootJar>("bootJar") {
    enabled = true
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("io.netty:netty-all:4.1.96.Final")
}
