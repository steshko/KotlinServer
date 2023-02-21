plugins {
    kotlin("jvm") version "1.8.0"
    application
    id("org.jetbrains.kotlin.plugin.serialization") version "1.5.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.ktor:ktor-server-core:2.2.3")
    implementation("io.ktor:ktor-server-netty:2.2.3")
    implementation ("ch.qos.logback:logback-classic:1.2.3")
    implementation("io.ktor:ktor-server-cors:2.2.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(18)
}

application {
    mainClass.set("MainKt")
}