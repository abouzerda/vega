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
    implementation(kotlin("stdlib-jdk8"))
    implementation("junit", "junit", "4.13.2")
    implementation("org.junit.jupiter", "junit-jupiter", "5.8.1")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

application {
    mainClass.set("MainKt")
}