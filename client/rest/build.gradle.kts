// 메인 클래스들을 포함한 jar를 만들고, '-plain.jar' 를 생성한다.
tasks.getByName<Jar>("jar") {
    enabled = true
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.apache.httpcomponents.core5:httpcore5:5.1.4")
    implementation("org.apache.httpcomponents.core5:httpcore5-h2:5.1.4")
    implementation("org.apache.httpcomponents.core5:httpcore5-reactive:5.1.4")
    implementation("org.apache.httpcomponents.client5:httpclient5:5.1.4")
    implementation("org.apache.httpcomponents.client5:httpclient5-cache:5.1.4")
}
