// 메인 클래스들을 포함한 jar를 만들고, '-plain.jar' 를 생성한다.
tasks.getByName<Jar>("jar") {
    enabled = true
}

dependencies {
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("org.bitbucket.b_c:jose4j:0.9.3")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")
//    implementation("com.bucket4j:bucket4j-core:8.7.0")
}
