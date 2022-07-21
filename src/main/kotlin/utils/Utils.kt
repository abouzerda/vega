package utils

import com.google.gson.GsonBuilder
import component.Component
import io.ComponentTypeAdapter
import java.io.FileNotFoundException

object Utils {
    val gson = GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(Component::class.java, ComponentTypeAdapter)
        .create()

    fun loadResource(filepath: String): String {
        return Utils::class.java.getResource(filepath)?.readText() ?: throw FileNotFoundException()
    }
}