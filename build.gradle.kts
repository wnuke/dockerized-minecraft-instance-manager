val kotlinVersion = "1.4-M3"
val ktorVersion = "1.3.2-$kotlinVersion"
val dockerJavaVersion = "3.2.5"

repositories {
    jcenter()
    mavenCentral()
    gradlePluginPortal()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    maven("https://dl.bintray.com/kotlin/kotlin-dev")
    maven("https://kotlin.bintray.com/kotlinx")
}

plugins {
    kotlin("jvm") version "1.4-M3"
    id("org.jetbrains.dokka") version "1.4-mc-1"
    kotlin("plugin.serialization") version "1.4-M3"
    application
}

val run by tasks.getting(JavaExec::class) {
    standardInput = System.`in`
}

tasks.register<org.jetbrains.dokka.gradle.DokkaTask>("dokkaJavadoc") {
    dependencies {
        // Using the javadoc plugin as "custom format". Can use any plugin here!
        plugins("org.jetbrains.dokka:javadoc-plugin:1.4-mc-1")
    }
    outputDirectory = "$buildDir/javadoc"
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Kotlinx Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")

    // Cli
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.2.1")

    // Docker
    implementation("com.github.docker-java:docker-java-core:$dockerJavaVersion")
    implementation("com.github.docker-java:docker-java-transport-httpclient5:$dockerJavaVersion")

    // Ktor
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-apache:$ktorVersion")
    implementation("io.ktor:ktor-client-json:$ktorVersion")
    implementation("io.ktor:ktor-client-serialization-jvm:$ktorVersion")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    mainClassName = "dev.wnuke.botmanager.AppKt"
}
