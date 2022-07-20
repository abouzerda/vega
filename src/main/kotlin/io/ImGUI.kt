package io

import core.GLFWWindow.height
import core.GLFWWindow.width
import imgui.ImGui
import imgui.callbacks.ImStrConsumer
import imgui.callbacks.ImStrSupplier
import imgui.enums.ImGuiBackendFlags
import imgui.enums.ImGuiConfigFlags
import imgui.gl3.ImGuiImplGl3
import org.lwjgl.glfw.GLFW.*

class ImGUI(private var glfwWindowHandle: Long) {
    private val lwjglRenderer: ImGuiImplGl3 = ImGuiImplGl3()

    fun init() {
        ImGui.createContext()
        val io = ImGui.getIO()
        /* We don't want to save .ini file */
        io.iniFilename = null
        /* Navigation with keyboard */
        io.configFlags = ImGuiConfigFlags.NavEnableKeyboard
        /* Mouse cursors to display while resizing windows etc. */
        io.backendFlags = ImGuiBackendFlags.HasMouseCursors
        io.backendPlatformName = "imgui_java_impl_glfw"
        /* Set keyboard mapping */
        io.setKeyMap(keyMap)
        /* Set up key input callbacks */
        glfwSetKeyCallback(glfwWindowHandle, KeyListener::imGuiKeyCallback)
        glfwSetCharCallback(glfwWindowHandle, KeyListener::imGuiCharCallback)
        /* Set up mouse input callbacks */
        glfwSetMouseButtonCallback(glfwWindowHandle, MouseListener::imGuiMouseButtonCallback)
        glfwSetScrollCallback(glfwWindowHandle, MouseListener::imGuiScrollCallback)
        /* Set up clipboard input callbacks */
        io.setSetClipboardTextFn(object : ImStrConsumer() {
            override fun accept(s: String) {
                glfwSetClipboardString(glfwWindowHandle, s)
            }
        })
        io.setGetClipboardTextFn(object : ImStrSupplier() {
            override fun get(): String {
                val clipboardString = glfwGetClipboardString(glfwWindowHandle)
                return clipboardString ?: ""
            }
        })
        lwjglRenderer.init("#version 400 core")
    }

    fun update(dt: Float) {
        startFrame(dt)
        ImGui.newFrame()
        ImGui.showDemoWindow()
        ImGui.render()
        endFrame()
    }

    private fun startFrame(dt: Float) {
        /* Get window properties and mouse position */
        val winWidth = floatArrayOf(width.toFloat())
        val winHeight = floatArrayOf(height.toFloat())
        val mousePosX = doubleArrayOf(0.0)
        val mousePosY = doubleArrayOf(0.0)
        glfwGetCursorPos(glfwWindowHandle, mousePosX, mousePosY)
        val io = ImGui.getIO()
        io.setDisplaySize(winWidth[0], winHeight[0])
        io.setDisplayFramebufferScale(1f, 1f)
        io.setMousePos(mousePosX[0].toFloat(), mousePosY[0].toFloat())
        io.deltaTime = dt
        /* Update the mouse cursor */
        val imguiCursor = ImGui.getMouseCursor()
        glfwSetCursor(glfwWindowHandle, mouseCursors[imguiCursor])
        glfwSetInputMode(glfwWindowHandle, GLFW_CURSOR, GLFW_CURSOR_NORMAL)
    }

    private fun endFrame() {
        lwjglRenderer.render(ImGui.getDrawData())
    }
}