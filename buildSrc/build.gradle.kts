plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation(group="org.jetbrains.kotlin", name = "kotlin-gradle-plugin", version = "1.7.10")
}