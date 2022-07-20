package core

import org.lwjgl.glfw.GLFW.*

object KeyListener {
    private var keys: Array<Boolean> = Array(KEY_RANGE) { false }

    fun pressedKey(keyCode: Int): Boolean {
        return keys[keyCode]
    }

    internal fun keyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
        val keyEvent = KeyEvent(key, glfwGetKeyName(key, scancode) ?: "", mods)
        when (action) {
            GLFW_PRESS -> GLFWWindow.currentScene.onKeyPress.invoke(keyEvent).also { keys[key] = true }
            GLFW_RELEASE -> GLFWWindow.currentScene.onKeyRelease.invoke(keyEvent).also { keys[key] = false }
            else -> {}
        }
    }
}
