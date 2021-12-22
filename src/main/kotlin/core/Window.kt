package core

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.system.MemoryUtil

typealias GLFWWindow = Long

class Window {
    companion object {
        val width: Int = 1920
        val height: Int = 1080
        var glfwWindow: GLFWWindow = 0
    }
    init {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set()
        // Initialize GLFW
        check(glfwInit()) { "Unable to initialize GLFW." }
        // Configure GLFW
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE)
        // Create the window
        glfwWindow = glfwCreateWindow(width, height, "", MemoryUtil.NULL, MemoryUtil.NULL)
        check(glfwWindow != MemoryUtil.NULL) { "Failed to create the GLFW window." }
        // Register key listener callback
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback)
        // Register mouse listener callback
        glfwSetCursorPosCallback(glfwWindow, MouseListener::cursorPosCallback)
        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow)
        // Enable v-sync
        glfwSwapInterval(1)
        // Make the window visible
        glfwShowWindow(glfwWindow)
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities()
    }
}