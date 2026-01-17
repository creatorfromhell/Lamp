plugins {
    id("java")
    id("com.gradleup.shadow") version "9.0.0-rc3"
}

group = "io.github.revxrsal"

val javaVersion = 25

val hytaleLib = file("lib/HytaleServer.jar")

java.toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))

    implementation("net.kyori:adventure-api:4.26.1")
    implementation("net.kyori:adventure-text-serializer-legacy:4.26.1")
    implementation("net.kyori:adventure-key:4.26.1")
    compileOnly(files(hytaleLib))
}

tasks {
    processResources {
        filesMatching("manifest.json") {
            expand(
                mapOf(
                    "version" to project.version,
                )
            )
        }
    }
}