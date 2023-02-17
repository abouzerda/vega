package io

import core.GLFWWindow
import core.GLFWWindow.height
import core.GLFWWindow.width
import imgui.*
import imgui.callback.ImStrConsumer
import imgui.callback.ImStrSupplier
import imgui.flag.*
import imgui.gl3.ImGuiImplGl3
import imgui.type.ImBoolean
import org.lwjgl.glfw.GLFW.*
import scene.Viewport
import utils.Assets
import java.util.logging.Logger

class ImGuiAdapter(private var glfwWindowHandle: Long) {
    private val logger: Logger = Logger.getLogger(javaClass.name)
    private val lwjglRenderer: ImGuiImplGl3 = ImGuiImplGl3()

    fun init() {
        this.logger.info("ImGui ${ImGui.getVersion()}!")
        ImGui.createContext()
        setStyles()
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
            /* TODO: Load resources properly */
            //fontAtlas.addFontFromFileTTF(IMGUI_FONT_FILE, IMGUI_FONT_SIZE, fontConfig)
            /* After all fonts were added we don't need this config more */
            fontConfig.destroy()
            //ImGuiFreeType.buildFontAtlas(fontAtlas)
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

    private fun setStyles() {
        val style = ImGui.getStyle()
        style.setColor(ImGuiCol.Text, 1.00f, 1.00f, 1.00f, 1.00f)
        style.setColor(ImGuiCol.TextDisabled, 0.50f, 0.50f, 0.50f, 1.00f)
        style.setColor(ImGuiCol.WindowBg, 0.13f, 0.14f, 0.15f, 1.00f)
        style.setColor(ImGuiCol.ChildBg, 0.13f, 0.14f, 0.15f, 1.00f)
        style.setColor(ImGuiCol.PopupBg, 0.13f, 0.14f, 0.15f, 1.00f)
        style.setColor(ImGuiCol.Border, 0.43f, 0.43f, 0.50f, 0.50f)
        style.setColor(ImGuiCol.BorderShadow, 0.00f, 0.00f, 0.00f, 0.00f)
        style.setColor(ImGuiCol.FrameBg, 0.25f, 0.25f, 0.25f, 1.00f)
        style.setColor(ImGuiCol.FrameBgHovered, 0.38f, 0.38f, 0.38f, 1.00f)
        style.setColor(ImGuiCol.FrameBgActive, 0.67f, 0.67f, 0.67f, 0.39f)
        style.setColor(ImGuiCol.TitleBg, 0.08f, 0.08f, 0.09f, 1.00f)
        style.setColor(ImGuiCol.TitleBgActive, 0.08f, 0.08f, 0.09f, 1.00f)
        style.setColor(ImGuiCol.TitleBgCollapsed, 0.00f, 0.00f, 0.00f, 0.51f)
        style.setColor(ImGuiCol.MenuBarBg, 0.14f, 0.14f, 0.14f, 1.00f)
        style.setColor(ImGuiCol.ScrollbarBg, 0.02f, 0.02f, 0.02f, 0.53f)
        style.setColor(ImGuiCol.ScrollbarGrab, 0.31f, 0.31f, 0.31f, 1.00f)
        style.setColor(ImGuiCol.ScrollbarGrabHovered, 0.41f, 0.41f, 0.41f, 1.00f)
        style.setColor(ImGuiCol.ScrollbarGrabActive, 0.51f, 0.51f, 0.51f, 1.00f)
        style.setColor(ImGuiCol.CheckMark, 0.11f, 0.64f, 0.92f, 1.00f)
        style.setColor(ImGuiCol.SliderGrab, 0.11f, 0.64f, 0.92f, 1.00f)
        style.setColor(ImGuiCol.SliderGrabActive, 0.08f, 0.50f, 0.72f, 1.00f)
        style.setColor(ImGuiCol.Button, 0.25f, 0.25f, 0.25f, 1.00f)
        style.setColor(ImGuiCol.ButtonHovered, 0.38f, 0.38f, 0.38f, 1.00f)
        style.setColor(ImGuiCol.ButtonActive, 0.67f, 0.67f, 0.67f, 0.39f)
        style.setColor(ImGuiCol.Header, 0.22f, 0.22f, 0.22f, 1.00f)
        style.setColor(ImGuiCol.HeaderHovered, 0.25f, 0.25f, 0.25f, 1.00f)
        style.setColor(ImGuiCol.HeaderActive, 0.67f, 0.67f, 0.67f, 0.39f)
        style.setColor(ImGuiCol.Separator, ImGuiCol.Border)
        style.setColor(ImGuiCol.SeparatorHovered, 0.41f, 0.42f, 0.44f, 1.00f)
        style.setColor(ImGuiCol.SeparatorActive, 0.26f, 0.59f, 0.98f, 0.95f)
        style.setColor(ImGuiCol.ResizeGrip, 0.00f, 0.00f, 0.00f, 0.00f)
        style.setColor(ImGuiCol.ResizeGripHovered, 0.29f, 0.30f, 0.31f, 0.67f)
        style.setColor(ImGuiCol.ResizeGripActive, 0.26f, 0.59f, 0.98f, 0.95f)
        style.setColor(ImGuiCol.Tab, 0.08f, 0.08f, 0.09f, 0.83f)
        style.setColor(ImGuiCol.TabHovered, 0.33f, 0.34f, 0.36f, 0.83f)
        style.setColor(ImGuiCol.TabActive, 0.23f, 0.23f, 0.24f, 1.00f)
        style.setColor(ImGuiCol.TabUnfocused, 0.08f, 0.08f, 0.09f, 1.00f)
        style.setColor(ImGuiCol.TabUnfocusedActive, 0.13f, 0.14f, 0.15f, 1.00f)
        style.setColor(ImGuiCol.DockingPreview, 0.26f, 0.59f, 0.98f, 0.70f)
        style.setColor(ImGuiCol.DockingEmptyBg, 0.20f, 0.20f, 0.20f, 1.00f)
        style.setColor(ImGuiCol.PlotLines, 0.61f, 0.61f, 0.61f, 1.00f)
        style.setColor(ImGuiCol.PlotLinesHovered, 1.00f, 0.43f, 0.35f, 1.00f)
        style.setColor(ImGuiCol.PlotHistogram, 0.90f, 0.70f, 0.00f, 1.00f)
        style.setColor(ImGuiCol.PlotHistogramHovered, 1.00f, 0.60f, 0.00f, 1.00f)
        style.setColor(ImGuiCol.TextSelectedBg, 0.26f, 0.59f, 0.98f, 0.35f)
        style.setColor(ImGuiCol.DragDropTarget, 0.11f, 0.64f, 0.92f, 1.00f)
        style.setColor(ImGuiCol.NavHighlight, 0.26f, 0.59f, 0.98f, 1.00f)
        style.setColor(ImGuiCol.NavWindowingHighlight, 1.00f, 1.00f, 1.00f, 0.70f)
        style.setColor(ImGuiCol.NavWindowingDimBg, 0.80f, 0.80f, 0.80f, 0.20f)
        style.setColor(ImGuiCol.ModalWindowDimBg, 0.80f, 0.80f, 0.80f, 0.35f)
        style.setWindowPadding(8.00f, 8.00f)
        style.setFramePadding(5.00f, 2.00f)
        style.setItemSpacing(6.00f, 6.00f)
        style.setItemInnerSpacing(6.00f, 6.00f)
        style.setTouchExtraPadding(0.00f, 0.00f)
        style.indentSpacing = 25f
        style.scrollbarSize = 15f
        style.grabMinSize = 10f
        style.windowBorderSize = 1f
        style.childBorderSize = 1f
        style.popupBorderSize = 1f
        style.frameBorderSize = 1f
        style.tabBorderSize = 1f
        style.windowRounding = 7f
        style.childRounding = 4f
        style.frameRounding = 10f
        style.popupRounding = 4f
        style.scrollbarRounding = 9f
        style.grabRounding = 3f
        style.tabRounding = 4f
    }

    fun update(dt: Float) {
        startFrame(dt)
        ImGui.newFrame()
        setupDockspace()
        //showFPS(dt)
        setupMainMenuBar()
        GLFWWindow.currentScene.imgui()
        Viewport.imgui()
        ImGui.end()
        ImGui.render()
        endFrame()
    }

    private fun setupMainMenuBar() {
        if (ImGui.beginMainMenuBar()) {
            if (ImGui.beginMenu("File")) ImGui.endMenu()
            if (ImGui.beginMenu("Edit")) ImGui.endMenu()
            ImGui.endMainMenuBar()
        }
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