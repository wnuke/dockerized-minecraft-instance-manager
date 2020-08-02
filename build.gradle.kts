/**
 * Version of Kotlin to use.
 */
val kotlinVersion: String = "1.4.0-rc"
/**
 * Version of Ktor to use
 */
val ktorVersion: String = "1.3.2-$kotlinVersion"
/**
 * Version of the Java Docker API to use
 */
val dockerJavaVersion: String = "3.2.5"

repositories {
    jcenter()
    mavenCentral()
    gradlePluginPortal()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    maven("https://dl.bintray.com/kotlin/kotlin-dev")
    maven("https://kotlin.bintray.com/kotlinx")
}

plugins {
    java
    application
    kotlin("jvm") version "1.4.0-rc"
    kotlin("plugin.serialization") version "1.4.0-rc"
    id("org.jetbrains.dokka") version "1.4.0-rc-21"
}

/**
 * Make the run task use System.in as standard input.
 */
val run: JavaExec by tasks.getting(JavaExec::class) {
    standardInput = System.`in`
}

tasks.register<org.jetbrains.dokka.gradle.DokkaTask>("dokkaJavadocCustom") {
    dependencies {
        // Using the javadoc plugin as "custom format". Can use any plugin here!
        plugins("org.jetbrains.dokka:javadoc-plugin:1.4.0-rc-21")
    }
    outputDirectory = "$buildDir/javadoc"
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:$kotlinVersion"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")

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
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
}

application {
    mainClassName = "dev.wnuke.botmanager.AppKt"
}