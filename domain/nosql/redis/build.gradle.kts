// 메인 클래스들을 포함한 jar를 만들고, '-plain.jar' 를 생성한다.
tasks.getByName<Jar>("jar") {
    enabled = true
}

dependencies {
    implementation(project(":common"))

    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("it.ozimov:embedded-redis:0.7.3") {
        // Exception in thread "main"
        // java.lang.IllegalArgumentException: LoggerFactory is not a Logback LoggerContext but Logback is on the classpath.
        // 위의 예외가 발생하지 않도록 slf4j-simple 모듈을 제외한다.
        exclude("org.slf4j", "slf4j-simple")
    }
}
