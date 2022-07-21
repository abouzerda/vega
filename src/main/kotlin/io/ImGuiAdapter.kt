package io

import core.GLFWWindow
import core.GLFWWindow.height
import core.GLFWWindow.width
import imgui.ImFontAtlas
import imgui.ImFontConfig
import imgui.ImGui
import imgui.ImGuiFreeType
import imgui.callback.ImStrConsumer
import imgui.callback.ImStrSupplier
import imgui.flag.*
import imgui.gl3.ImGuiImplGl3
import imgui.type.ImBoolean
import org.lwjgl.glfw.GLFW.*
import java.util.logging.Logger

class ImGuiAdapter(private var glfwWindowHandle: Long) {
    private val logger: Logger = Logger.getLogger(javaClass.name)
    private val lwjglRenderer: ImGuiImplGl3 = ImGuiImplGl3()

    fun init() {
        this.logger.info("ImGui ${ImGui.getVersion()}!")
        ImGui.createContext()
        with(ImGui.getIO()) {
            /* We don't want to save .ini file */
            iniFilename = IMGUI_INI_FILE
            /* Navigation with keyboard */
            addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard)
            /* Enable window docking */
            addConfigFlags(ImGuiConfigFlags.DockingEnable)
            /* Mouse cursors to display while resizing windows etc. */
            addBackendFlags(ImGuiBackendFlags.HasMouseCursors)
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
        setupDockspace()
        //showFPS(dt)
        GLFWWindow.currentScene.imgui()
        //ImGui.showDemoWindow()
        ImGui.end()
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

    private fun setupDockspace() {
        var windowFlags = ImGuiWindowFlags.MenuBar or ImGuiWindowFlags.NoDocking
        ImGui.setNextWindowPos(0.0f, 0.0f, ImGuiCond.Always)
        ImGui.setNextWindowSize(width.toFloat(), height.toFloat())
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f)
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f)
        windowFlags = windowFlags or (ImGuiWindowFlags.NoTitleBar or ImGuiWindowFlags.NoCollapse or
                ImGuiWindowFlags.NoResize or ImGuiWindowFlags.NoMove or
                ImGuiWindowFlags.NoBringToFrontOnFocus or ImGuiWindowFlags.NoNavFocus)
        ImGui.begin("Dockspace", ImBoolean(true), windowFlags)
        ImGui.popStyleVar(2)
        ImGui.dockSpace(ImGui.getID("Dockspace"))
    }
}