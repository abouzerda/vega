package scene

import core.GLFWWindow
import imgui.ImGui
import imgui.ImVec2
import imgui.flag.ImGuiWindowFlags

object Viewport {
    fun imgui() {
        ImGui.begin("Viewport", ImGuiWindowFlags.NoScrollbar or ImGuiWindowFlags.NoScrollWithMouse)
        val windowSize = largestSizeForViewport
        val windowPos = getCenteredPositionForViewport(windowSize)
        ImGui.setCursorPos(windowPos.x, windowPos.y)
        val textureId: Int = GLFWWindow.frameBuffer.textureId
        ImGui.image(textureId, windowSize.x, windowSize.y, 0f, 1f, 1f, 0f)
        ImGui.end()
    }

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