plugins {
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    maven {
        url = uri("https://maven.pkg.github.com/Dominaezzz/kgl")
        credentials {
            username = "heaterscar"
            password = "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"
        }
    }
}
