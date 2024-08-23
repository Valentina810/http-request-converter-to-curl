plugins {
    kotlin("jvm") version "1.9.23"
    `maven-publish`
}

group = "com.github.valentina810"
version = "1.0.0"

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            groupId = "com.github.valentina810"
            artifactId = "http-request-converter-to-curl"
            version = "1.0.0"
        }
    }
    repositories {
        mavenLocal()
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("jakarta.servlet:jakarta.servlet-api:6.1.0")
    implementation("org.slf4j:slf4j-api:2.0.12")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testImplementation("org.mockito:mockito-core:5.11.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}