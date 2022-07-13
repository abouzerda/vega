package utils

class Utils {
    companion object {
        fun loadResource(filepath : String): String {
            return Utils::class.java.getResource(filepath).readText()
        }

        @JvmStatic
        fun loadResource_(filepath: String): String {
            return Utils::class.java.getResource(filepath).readText()
        }
    }
}