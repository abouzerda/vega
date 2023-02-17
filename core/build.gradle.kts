plugins {
    id("executable-conventions")
}

val lwjglVersion = "3.2.3"
val lwjglNatives = "natives-linux"
val jomlVersion = "1.10.2"
val imguiVersion = "1.77-0.17.1"
val gsonVersion = "2.9.0"

kotlin {
    jvm {
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation(project.dependencies.platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))
                implementation("org.lwjgl:lwjgl")
                implementation("org.lwjgl:lwjgl-assimp")
                implementation("org.lwjgl:lwjgl-glfw")
                implementation("org.lwjgl:lwjgl-nfd")
                implementation("org.lwjgl:lwjgl-openal")
                implementation("org.lwjgl:lwjgl-opengl")
                implementation("org.lwjgl:lwjgl-stb")

                runtimeOnly("org.lwjgl:lwjgl:$lwjglVersion:$lwjglNatives")
                runtimeOnly("org.lwjgl:lwjgl-assimp:$lwjglVersion:$lwjglNatives")
                runtimeOnly("org.lwjgl:lwjgl-glfw:$lwjglVersion:$lwjglNatives")
                runtimeOnly("org.lwjgl:lwjgl-nfd:$lwjglVersion:$lwjglNatives")
                runtimeOnly("org.lwjgl:lwjgl-openal:$lwjglVersion:$lwjglNatives")
                runtimeOnly("org.lwjgl:lwjgl-opengl:$lwjglVersion:$lwjglNatives")
                runtimeOnly("org.lwjgl:lwjgl-stb:$lwjglVersion:$lwjglNatives")

                implementation("org.joml:joml:$jomlVersion")
                implementation("io.imgui.java:binding:$imguiVersion")
                implementation("io.imgui.java:lwjgl3:$imguiVersion")
                runtimeOnly("io.imgui.java:natives-linux:$imguiVersion")
                implementation("com.google.code.gson:gson:$gsonVersion")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
                implementation("org.junit.jupiter:junit-jupiter:5.8.1")
            }
        }
        val nativeMain by getting {
            dependencies {
            }
        }
    }
}