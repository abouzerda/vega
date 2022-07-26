package io

import core.Camera
import core.GLFWWindow
import imgui.ImGui
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector4f
import org.lwjgl.glfw.GLFW.*
import scene.Viewport
import java.util.*

object MouseListener {
    var cursorPosX: Double = 0.0
    var cursorPosY: Double = 0.0

    val normalizedX: Double
        get() = 2 * ((cursorPosX - viewportPos.x) / viewportSize.x) - 1
    val normalizedY: Double
        get() = 2 * ((cursorPosY - viewportPos.y) / viewportSize.y) - 1

    /*
    val cursorOrthoX: Double
        get() = Vector4d(normalizedX, 0.0, 0.0, 1.0)
            .mul(GLFWWindow.currentScene.camera.inverseViewMatrix)
            .mul(GLFWWindow.currentScene.camera.inverseProjectionMatrix).x
    val cursorOrthoY: Double
        get() = Vector4d(0.0, normalizedY, 0.0, 1.0)
            .mul(GLFWWindow.currentScene.camera.inverseViewMatrix)
            .mul(GLFWWindow.currentScene.camera.inverseProjectionMatrix).y
    */

    val cursorOrthoX: Double
        get() {
            var currentX: Double = cursorPosX - viewportPos.x
            currentX = currentX / viewportSize.x * 2.0f - 1.0f
            val tmp = Vector4f(currentX.toFloat(), 0f, 0f, 1f)
            val camera: Camera = GLFWWindow.currentScene.camera
            val viewProjection = Matrix4f()
            camera.inverseViewMatrix.mul(camera.inverseProjectionMatrix, viewProjection)
            tmp.mul(viewProjection)
            currentX = tmp.x.toDouble()
            return currentX
        }

    val cursorOrthoY: Double
        get() {
            var currentY: Double = cursorPosY - viewportPos.y
            currentY = -(currentY / viewportSize.y * 2.0f - 1.0f)
            val tmp = Vector4f(0f, currentY.toFloat(), 0f, 1f)
            val camera: Camera = GLFWWindow.currentScene.camera
            val viewProjection = Matrix4f()
            camera.inverseViewMatrix.mul(camera.inverseProjectionMatrix, viewProjection)
            tmp.mul(viewProjection)
            currentY = tmp.y.toDouble()
            return currentY
        }

    var viewportPos = Vector2f()
    var viewportSize = Vector2f()

    private val io
        get() = ImGui.getIO()

    private var buttons: Array<Boolean> = Array(GLFW_MOUSE_BUTTON_LAST + 1) { false }

    fun pressedButton(button: Int): Boolean {
        return buttons[button]
    }

    internal fun sceneCursorPositionCallback(window: Long, xPos: Double, yPos: Double) {
        cursorPosX = xPos
        cursorPosY = yPos
        GLFWWindow.currentScene.onMouseMove.invoke(MouseEvent(xPos = xPos, yPos = yPos))
    }

    internal fun sceneCursorEnterCallback(window: Long, entered: Boolean) {
        if (entered) GLFWWindow.currentScene.onMouseEnter.invoke(MouseEvent())
        else GLFWWindow.currentScene.onMouseExit.invoke(MouseEvent())
    }

    internal fun sceneMouseButtonCallback(window: Long, button: Int, action: Int, mods: Int) {
        val mouseEvent = MouseEvent(button = button, action = Optional.of(action), mods = mods)
        when (action) {
            GLFW_PRESS -> GLFWWindow.currentScene.onMousePress.invoke(mouseEvent).also { buttons[button] = true }
            GLFW_RELEASE -> GLFWWindow.currentScene.onMouseRelease.invoke(mouseEvent).also { buttons[button] = false }
            else -> {}
        }
    }

    internal fun sceneScrollCallback(window: Long, xOffset: Double, yOffset: Double) {
        GLFWWindow.currentScene.onMouseScroll.invoke(MouseEvent(xScrollOffset = xOffset, yScrollOffset = yOffset))
    }

    internal fun imGuiMouseButtonCallback(window: Long, button: Int, action: Int, mods: Int) {
        val mouseDown = BooleanArray(5) { button == it && action != GLFW_RELEASE }
        io.setMouseDown(mouseDown)
        if (!io.wantCaptureMouse && mouseDown[1]) ImGui.setWindowFocus(null)
        if (!io.wantCaptureMouse || Viewport.wantCaptureMouse) sceneMouseButtonCallback(window, button, action, mods)
    }

    internal fun imGuiScrollCallback(window: Long, xOffset: Double, yOffset: Double) {
        io.mouseWheelH = io.mouseWheelH + xOffset.toFloat()
        io.mouseWheel = io.mouseWheel + yOffset.toFloat()
    }
}