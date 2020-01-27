plugins {
    kotlin("jvm") version "1.3.61"
}

repositories {
    jcenter()
}

dependencies {
    implementation(platform(kotlin("bom")))
    implementation(kotlin("stdlib-jdk8"))

    implementation("com.google.guava:guava:28.2-jre")

    // tests
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "11"
}