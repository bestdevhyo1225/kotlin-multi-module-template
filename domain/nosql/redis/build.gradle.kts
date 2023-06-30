// 메인 클래스들을 포함한 jar를 만들고, '-plain.jar' 를 생성한다.
tasks.getByName<Jar>("jar") {
    enabled = true
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("it.ozimov:embedded-redis:0.7.3") {
        // Exception in thread "main"
        // java.lang.IllegalArgumentException: LoggerFactory is not a Logback LoggerContext but Logback is on the classpath.
        // 위의 예외가 발생하지 않도록 slf4j-simple 모듈을 제외한다.
        exclude("org.slf4j", "slf4j-simple")
    }
    // Embedded Redis 라이브러리 사용시, 해결하기 위한 com.google.guava 의존성 수정
    // CWE-770 - Allocation of Resources Without Limits or Throttling
    implementation("com.google.guava:guava:32.0.1-jre")
    // Embedded Redis 라이브러리 사용시, 해결하기 위한 common-ios:commons-io 의존성 수정
    // 4.8 Improper Limitation of a Pathname to a Restricted Directory ('Path Traversal') vulnerability pending CVSS allocation
    implementation("commons-io:commons-io:2.13.0")
}
