// 메인 클래스들을 포함한 jar를 만들고, '-plain.jar' 를 생성한다.
tasks.getByName<Jar>("jar") {
    enabled = true
}

dependencies {
    implementation("org.springframework.kafka:spring-kafka")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
}
