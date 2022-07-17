package utils

import java.io.FileNotFoundException

class Utils {
    companion object {
        fun loadResource(filepath: String): String {
            return Utils::class.java.getResource(filepath)?.readText() ?: throw FileNotFoundException()
        }
    }
}