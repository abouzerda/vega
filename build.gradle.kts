plugins {
    kotlin("jvm") version "1.4.21"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

val lwjglVersion = "3.2.3"
val lwjglNatives = "natives-linux"
val jomlVersion = "1.10.2"
val imguiVersion = "1.77-0.17.1"
val gsonVersion = "2.9.0"

application {
    mainClassName = "MainKt"
}

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("junit", "junit", "4.13.2")
    implementation("org.junit.jupiter", "junit-jupiter", "5.8.1")

    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    implementation("org.lwjgl", "lwjgl")
    implementation("org.lwjgl", "lwjgl-assimp")
    implementation("org.lwjgl", "lwjgl-glfw")
    implementation("org.lwjgl", "lwjgl-nfd")
    implementation("org.lwjgl", "lwjgl-openal")
    implementation("org.lwjgl", "lwjgl-opengl")
    implementation("org.lwjgl", "lwjgl-stb")
    runtimeOnly("org.lwjgl", "lwjgl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-assimp", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-nfd", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-openal", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-stb", classifier = lwjglNatives)

    implementation("org.joml", "joml", jomlVersion)

    implementation("io.imgui.java", "binding", imguiVersion)
    implementation("io.imgui.java", "lwjgl3", imguiVersion)
    runtimeOnly("io.imgui.java", "natives-linux", imguiVersion)

    implementation("com.google.code.gson", "gson", gsonVersion)
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}