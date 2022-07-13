package core

import org.lwjgl.glfw.GLFW.*

class KeyListener {
    companion object {
        private var keys : Array<Boolean> = Array(KEY_RANGE) { false }

        fun pressedKey(keyCode : Int): Boolean {
            return keys[keyCode]
        }

        fun keyCallback(window: GLFWWindow, key: Int, scanCode: Int, action: Int, mods: Int) {
            when(action) {
                GLFW_PRESS -> keys[key] = true
                GLFW_RELEASE -> keys[key] = false
            }
        }
    }
}
