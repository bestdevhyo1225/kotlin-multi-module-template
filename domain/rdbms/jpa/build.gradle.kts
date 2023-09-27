// 메인 클래스들을 포함한 jar를 만들고, '-plain.jar' 를 생성한다.
tasks.getByName<Jar>("jar") {
    enabled = true
}

apply(plugin = "kotlin-jpa")
apply(plugin = "kotlin-allopen")

allOpen {
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.data:spring-data-envers")
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    implementation("org.mindrot:jbcrypt:0.4")

    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
    kapt("jakarta.persistence:jakarta.persistence-api")
    kapt("jakarta.annotation:jakarta.annotation-api")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")
}

kotlin.sourceSets.main {
    setBuildDir("$buildDir")
}
