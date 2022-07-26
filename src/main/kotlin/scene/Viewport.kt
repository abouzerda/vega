package scene

import core.GLFWWindow
import imgui.ImGui
import imgui.ImVec2
import imgui.flag.ImGuiWindowFlags
import io.MouseListener
import org.joml.Vector2f

object Viewport {

    var leftX = 0f
    var rightX = 0f
    var topY = 0f
    var bottomY = 0f

    fun imgui() {
        ImGui.begin("Viewport", ImGuiWindowFlags.NoScrollbar or ImGuiWindowFlags.NoScrollWithMouse)
        val windowSize = largestSizeForViewport
        val windowPos = getCenteredPositionForViewport(windowSize)
        ImGui.setCursorPos(windowPos.x, windowPos.y)

        val topLeft = ImVec2()
        ImGui.getCursorScreenPos(topLeft)
        topLeft.x -= ImGui.getScrollX()
        topLeft.y -= ImGui.getScrollY()
        leftX = topLeft.x
        bottomY = topLeft.y
        rightX = topLeft.x + windowSize.x
        topY = topLeft.y + windowSize.y

        val textureId: Int = GLFWWindow.frameBuffer.textureId
        ImGui.image(textureId, windowSize.x, windowSize.y, 0f, 1f, 1f, 0f)

        MouseListener.viewportPos = Vector2f(topLeft.x, topLeft.y)
        MouseListener.viewportSize = Vector2f(windowSize.x, windowSize.y)

        ImGui.end()
    }

    val wantCaptureMouse: Boolean
        get() = MouseListener.cursorPosX in leftX..rightX && MouseListener.cursorPosY in bottomY..topY


    private val largestSizeForViewport: ImVec2
        get() {
            val windowSize = ImVec2()
            ImGui.getContentRegionAvail(windowSize)
            windowSize.x -= ImGui.getScrollX()
            windowSize.y -= ImGui.getScrollY()
            var aspectWidth = windowSize.x
            var aspectHeight: Float = aspectWidth / GLFWWindow.targetAspectRatio
            if (aspectHeight > windowSize.y) {
                aspectHeight = windowSize.y
                aspectWidth = aspectHeight * GLFWWindow.targetAspectRatio
            }
            return ImVec2(aspectWidth, aspectHeight)
        }

    private fun getCenteredPositionForViewport(aspectSize: ImVec2): ImVec2 {
        val windowSize = ImVec2()
        ImGui.getContentRegionAvail(windowSize)
        windowSize.x -= ImGui.getScrollX()
        windowSize.y -= ImGui.getScrollY()
        val viewportX = windowSize.x / 2.0f - aspectSize.x / 2.0f
        val viewportY = windowSize.y / 2.0f - aspectSize.y / 2.0f
        return ImVec2(
            viewportX + ImGui.getCursorPosX(),
            viewportY + ImGui.getCursorPosY()
        )
    }
}