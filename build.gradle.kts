plugins {
    java
    id("org.springframework.boot") version "3.5.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
}

group = "yun"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

val snippetsDir by extra { file("build/generated-snippets") }

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    testImplementation("com.h2database:h2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    annotationProcessor("org.projectlombok:lombok")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.13")
}

tasks.withType<Test> {
    outputs.dir(snippetsDir)
    useJUnitPlatform()
}


tasks.test {
    outputs.dir(snippetsDir)
    useJUnitPlatform()
}

tasks.asciidoctor {
    inputs.dir(snippetsDir)
    dependsOn(tasks.test)
}

tasks.apply {
    bootJar {
        // 실행 가능한 JAR 생성 설정
        enabled = true
    }
    jar {
        // 일반 JAR 파일 생성 비활성화
        enabled = false
    }
}