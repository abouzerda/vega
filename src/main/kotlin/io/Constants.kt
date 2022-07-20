package io

import imgui.enums.ImGuiKey
import imgui.enums.ImGuiMouseCursor
import org.lwjgl.glfw.GLFW.*

const val GLSL_VERSION: String = "#version 400 core"

val keyMap = IntArray(ImGuiKey.COUNT).apply {
    set(ImGuiKey.Tab, GLFW_KEY_TAB)
    set(ImGuiKey.LeftArrow, GLFW_KEY_LEFT)
    set(ImGuiKey.RightArrow, GLFW_KEY_RIGHT)
    set(ImGuiKey.UpArrow, GLFW_KEY_UP)
    set(ImGuiKey.DownArrow, GLFW_KEY_DOWN)
    set(ImGuiKey.PageUp, GLFW_KEY_PAGE_UP)
    set(ImGuiKey.PageDown, GLFW_KEY_PAGE_DOWN)
    set(ImGuiKey.Home, GLFW_KEY_HOME)
    set(ImGuiKey.End, GLFW_KEY_END)
    set(ImGuiKey.Insert, GLFW_KEY_INSERT)
    set(ImGuiKey.Delete, GLFW_KEY_DELETE)
    set(ImGuiKey.Backspace, GLFW_KEY_BACKSPACE)
    set(ImGuiKey.Space, GLFW_KEY_SPACE)
    set(ImGuiKey.Enter, GLFW_KEY_ENTER)
    set(ImGuiKey.Escape, GLFW_KEY_ESCAPE)
    set(ImGuiKey.KeyPadEnter, GLFW_KEY_KP_ENTER)
    set(ImGuiKey.A, GLFW_KEY_A)
    set(ImGuiKey.C, GLFW_KEY_C)
    set(ImGuiKey.V, GLFW_KEY_V)
    set(ImGuiKey.X, GLFW_KEY_X)
    set(ImGuiKey.Y, GLFW_KEY_Y)
    set(ImGuiKey.Z, GLFW_KEY_Z)
}

val mouseCursors = LongArray(ImGuiMouseCursor.COUNT).apply {
    set(ImGuiMouseCursor.Arrow, glfwCreateStandardCursor(GLFW_ARROW_CURSOR))
    set(ImGuiMouseCursor.TextInput, glfwCreateStandardCursor(GLFW_IBEAM_CURSOR))
    set(ImGuiMouseCursor.ResizeAll, glfwCreateStandardCursor(GLFW_ARROW_CURSOR))
    set(ImGuiMouseCursor.ResizeNS, glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR))
    set(ImGuiMouseCursor.ResizeEW, glfwCreateStandardCursor(GLFW_HRESIZE_CURSOR))
    set(ImGuiMouseCursor.ResizeNESW, glfwCreateStandardCursor(GLFW_ARROW_CURSOR))
    set(ImGuiMouseCursor.ResizeNWSE, glfwCreateStandardCursor(GLFW_ARROW_CURSOR))
    set(ImGuiMouseCursor.Hand, glfwCreateStandardCursor(GLFW_HAND_CURSOR))
    set(ImGuiMouseCursor.NotAllowed, glfwCreateStandardCursor(GLFW_ARROW_CURSOR))
}
