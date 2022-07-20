package core

import org.lwjgl.glfw.GLFW
import java.util.*

object MouseListener {
    var cursorPosX: Double = 0.0
    var cursorPosY: Double = 0.0

    internal fun cursorPositionCallback(window: Long, xPos: Double, yPos: Double) {
        cursorPosX = xPos
        cursorPosY = yPos
        GLFWWindow.currentScene.onMouseMove.invoke(MouseEvent(xPos = xPos, yPos = yPos))
    }

    internal fun cursorEnterCallback(window: Long, entered: Boolean) {
        if (entered) GLFWWindow.currentScene.onMouseEnter.invoke(MouseEvent())
        else GLFWWindow.currentScene.onMouseExit.invoke(MouseEvent())
    }

    internal fun mouseButtonCallback(window: Long, button: Int, action: Int, mods: Int) {
        val mouseEvent = MouseEvent(button = button, action = Optional.of(action), mods = mods)
        when (action) {
            GLFW.GLFW_PRESS -> GLFWWindow.currentScene.onMousePress.invoke(mouseEvent)
            GLFW.GLFW_RELEASE -> GLFWWindow.currentScene.onMouseRelease.invoke(mouseEvent)
            else -> {}
        }
    }

    internal fun scrollCallback(window: Long, xOffset: Double, yOffset: Double) {
        GLFWWindow.currentScene.onMouseScroll.invoke(MouseEvent(xScrollOffset = xOffset, yScrollOffset = yOffset))
    }
}