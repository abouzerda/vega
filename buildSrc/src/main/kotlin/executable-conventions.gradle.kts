import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests

plugins {
    id("native-conventions")
}

kotlin {
    targets.asMap.forEach {
        val nativeTarget = it.value
        if(nativeTarget is KotlinNativeTargetWithHostTests) {
            nativeTarget.binaries {
                executable()
            }
        }
    }
}

