import core.GameObject

object Prefabs {
    fun generateTestObject() = GameObject("Test Game Object ${GameObject.ID_COUNTER + 1}")
}