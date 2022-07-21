package io

import core.GLFWWindow
import core.GLFWWindow.height
import core.GLFWWindow.width
import imgui.ImFontAtlas
import imgui.ImFontConfig
import imgui.ImGui
import imgui.ImGuiFreeType
import imgui.callbacks.ImStrConsumer
import imgui.callbacks.ImStrSupplier
import imgui.enums.ImGuiBackendFlags
import imgui.enums.ImGuiConfigFlags
import imgui.gl3.ImGuiImplGl3
import org.lwjgl.glfw.GLFW.*
import java.util.logging.Logger
import kotlin.math.cos

class ImGUI(private var glfwWindowHandle: Long) {
    private val logger: Logger = Logger.getLogger(javaClass.name)
    private val lwjglRenderer: ImGuiImplGl3 = ImGuiImplGl3()

    fun init() {
        this.logger.info("ImGui ${ImGui.getVersion()}!")
        ImGui.createContext()
        with(ImGui.getIO()) {
            /* We don't want to save .ini file */
            iniFilename = IMGUI_INI_FILE
            /* Navigation with keyboard */
            configFlags = ImGuiConfigFlags.NavEnableKeyboard
            /* Mouse cursors to display while resizing windows etc. */
            backendFlags = ImGuiBackendFlags.HasMouseCursors
            backendPlatformName = IMGUI_PLATFORM_NAME
            /* Set keyboard mapping */
            setKeyMap(keyMap)
            /* Set up clipboard input callbacks */
            setSetClipboardTextFn(object : ImStrConsumer() {
                override fun accept(s: String) {
                    glfwSetClipboardString(glfwWindowHandle, s)
                }
            })
            setGetClipboardTextFn(object : ImStrSupplier() {
                override fun get(): String {
                    val clipboardString = glfwGetClipboardString(glfwWindowHandle)
                    return clipboardString ?: ""
                }
            })
            val fontAtlas: ImFontAtlas = fonts
            /* Natively allocated object, should be explicitly destroyed */
            val fontConfig = ImFontConfig()
            /* Glyphs could be added per-font as well as per config used globally like here */
            fontConfig.glyphRanges = fontAtlas.glyphRangesDefault
            /* Fonts merge example */
            fontConfig.pixelSnapH = true
            /* Fonts from file/memory example */
            fontAtlas.addFontFromFileTTF(IMGUI_FONT_FILE, IMGUI_FONT_SIZE, fontConfig)
            /* After all fonts were added we don't need this config more */
            fontConfig.destroy()
            ImGuiFreeType.buildFontAtlas(fontAtlas)
        }
        /* Set up key input callbacks */
        glfwSetKeyCallback(glfwWindowHandle, KeyListener::imGuiKeyCallback)
        glfwSetCharCallback(glfwWindowHandle, KeyListener::imGuiCharCallback)
        /* Set up mouse input callbacks */
        glfwSetMouseButtonCallback(glfwWindowHandle, MouseListener::imGuiMouseButtonCallback)
        glfwSetScrollCallback(glfwWindowHandle, MouseListener::imGuiScrollCallback)

        this.logger.info("GLSL ${GLSL_VERSION.removePrefix("#")}!")
        lwjglRenderer.init(GLSL_VERSION)
    }

    fun update(dt: Float) {
        startFrame(dt)
        ImGui.newFrame()
        //showFPS(dt)
        GLFWWindow.currentScene.sceneImgui()
        //ImGui.showDemoWindow()
        ImGui.render()
        endFrame()
    }

    private var values: FloatArray = FloatArray(10)
    private var valuesCount: Int = 10
    private var refreshTime: Double = 0.0
    private var valuesOffset: Int = 0
    private var fps = ""

    private fun showFPS(dt: Float) {
        if (fps == "") fps = "%.2f FPS".format(1 / dt)
        valuesOffset = (valuesOffset + 1) % values.size
        refreshTime += dt
        if (refreshTime >= 0.5) {
            fps = "%.2f FPS".format(1 / dt)
            refreshTime = 0.0
            values = FloatArray(valuesCount) {
                if (it != valuesCount - 1) values[it + 1]
                else 1 / dt
            }
        }
        ImGui.plotLines("", values, valuesCount, 0, fps, 0f, 80f, 0f, 80f)
    }

    private fun startFrame(dt: Float) {
        /* Get window properties and mouse position */
        val winWidth = floatArrayOf(width.toFloat())
        val winHeight = floatArrayOf(height.toFloat())
        val mousePosX = doubleArrayOf(0.0)
        val mousePosY = doubleArrayOf(0.0)
        glfwGetCursorPos(glfwWindowHandle, mousePosX, mousePosY)
        with(ImGui.getIO()) {
            setDisplaySize(winWidth[0], winHeight[0])
            setDisplayFramebufferScale(1f, 1f)
            setMousePos(mousePosX[0].toFloat(), mousePosY[0].toFloat())
            deltaTime = dt
        }
        /* Update the mouse cursor */
        val imguiCursor = ImGui.getMouseCursor()
        glfwSetCursor(glfwWindowHandle, mouseCursors[imguiCursor])
        glfwSetInputMode(glfwWindowHandle, GLFW_CURSOR, GLFW_CURSOR_NORMAL)
    }

    private fun endFrame() {
        lwjglRenderer.render(ImGui.getDrawData())
    }
}