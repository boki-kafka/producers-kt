plugins {
    kotlin("jvm") version "2.0.10"
}

group = "com.boki"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    // https://mvnrepository.com/artifact/org.apache.kafka/kafka-clients
    implementation("org.apache.kafka:kafka-clients:3.6.1")
    // https://mvnrepository.com/artifact/io.github.oshai/kotlin-logging
    runtimeOnly("io.github.oshai:kotlin-logging:7.0.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
