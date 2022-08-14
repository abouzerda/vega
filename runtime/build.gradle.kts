plugins {
    kotlin("jvm")
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(rootProject)
    implementation(project(":core"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("junit", "junit", "4.13.2")
    implementation("org.junit.jupiter", "junit-jupiter", "5.8.1")
}

application {
    mainClass.set("MainKt")
}