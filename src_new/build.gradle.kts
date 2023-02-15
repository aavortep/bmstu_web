val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

tasks.generateReDoc.configure {
    inputFile = file("$rootDir/src/main/resources/swagger/swagger.yaml")
    outputDir = file("$rootDir/src/main/resources/doc")
    title = "Api Doc"
    options = mapOf(
        "spec-url" to "http://localhost:8080/api/v1/doc/swagger.yaml"
    )
}

plugins {
    application
    kotlin("jvm") version "1.7.20"
    id("io.ktor.plugin") version "2.1.2"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.21"
    id("org.hidetake.swagger.generator") version "2.19.2"
}

group = "com.example"
version = "0.0.1"
application {
    mainClass.set("com.example.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

val exposed_version: String by project

dependencies {
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-auth-jvm:2.1.2")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:2.1.2")
    implementation("io.ktor:ktor-server-cors-jvm:2.1.2")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("com.google.code.gson:gson:2.9.1")
}