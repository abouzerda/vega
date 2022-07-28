package io

import core.GLFWWindow
import imgui.ImGui
import org.lwjgl.glfw.GLFW.*

object KeyListener {
    private var keys: Array<Boolean> = Array(GLFW_KEY_LAST + 1) { false }

    private val io
        get() = ImGui.getIO()

    fun pressedKey(keyCode: Int): Boolean {
        return keys[keyCode]
    }

    internal fun sceneKeyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
        val keyEvent = KeyEvent(key, glfwGetKeyName(key, scancode) ?: "", mods)
        when (action) {
            GLFW_PRESS -> GLFWWindow.currentScene.onKeyPress.invoke(keyEvent).also { keys[key] = true }
            GLFW_RELEASE -> GLFWWindow.currentScene.onKeyRelease.invoke(keyEvent).also { keys[key] = false }
            else -> {}
        }
    }

    internal fun imGuiKeyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
        when (action) {
            GLFW_PRESS -> io.setKeysDown(key, true)
            GLFW_RELEASE -> io.setKeysDown(key, false)
        }
        io.keyCtrl = io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL)
        io.keyShift = io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT)
        io.keyAlt = io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT)
        io.keySuper = io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER)

        sceneKeyCallback(window, key, scancode, action, mods)
    }

    internal fun imGuiCharCallback(window: Long, character: Int) {
        if (character != GLFW_KEY_DELETE) io.addInputCharacter(character)
    }
}
