import org.springframework.boot.gradle.tasks.bundling.BootJar

// 메인 클래스들과 디펜던시를 포함해 실행 가능한 jar를 만든다.
tasks.getByName<BootJar>("bootJar") {
    enabled = true
}

dependencies {
    implementation(project(":common"))
    implementation(project(":domain:nosql:redis"))

    implementation("org.springframework.cloud:spring-cloud-starter-gateway:4.0.7")
    implementation("org.yaml:snakeyaml:2.2")
    implementation("io.projectreactor.tools:blockhound:1.0.8.RELEASE")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
}
