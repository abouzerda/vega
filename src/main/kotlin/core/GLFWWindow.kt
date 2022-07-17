package core

import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.system.MemoryUtil.NULL
import scene.MainScene
import java.util.logging.Logger

object GLFWWindow {
    private val logger: Logger = Logger.getLogger(javaClass.name)

    private const val width = 1920
    private const val height = 1080
    private const val title = ""

    private var glfwWindowHandle = 0L

    internal var currentScene: Scene = MainScene()

    fun showScene(scene: Scene) {
        this.currentScene = scene
    }

    internal fun shouldClose() : Boolean = glfwWindowShouldClose(glfwWindowHandle)

    internal fun init() {
        /* Set up error callback */
        GLFWErrorCallback.createPrint(System.err).set()
        /* Initialize GLFW */
        if (!glfwInit()) logger.severe("Unable to initialize GLFW!")
        /* Configure GLFW */
        glfwDefaultWindowHints()
        /* Create the window handle */
        glfwWindowHandle = glfwCreateWindow(width, height, title, NULL, NULL)
        if (glfwWindowHandle == NULL) logger.severe("Failed to create GLFW window!")
        /* Set up key input callbacks */
        glfwSetKeyCallback(glfwWindowHandle, KeyListener::keyCallback)
        /* Set up mouse input callbacks */
        glfwSetMouseButtonCallback(glfwWindowHandle, MouseListener::mouseButtonCallback)
        glfwSetCursorPosCallback(glfwWindowHandle, MouseListener::cursorPositionCallback)
        glfwSetCursorEnterCallback(glfwWindowHandle, MouseListener::cursorEnterCallback)
        glfwSetScrollCallback(glfwWindowHandle, MouseListener::scrollCallback)
        /* Make OpenGL the current context */
        glfwMakeContextCurrent(glfwWindowHandle)
        /* Enable V-Sync */
        glfwSwapInterval(1)

        GL.createCapabilities()
    }

    internal fun update(dt: Float?) {
        /* Poll events */
        glfwPollEvents()
        if (dt != null) this.currentScene.update(dt)

        glfwSwapBuffers(glfwWindowHandle)
    }

    internal fun close() {
        /* Clean up memory */
        glfwFreeCallbacks(glfwWindowHandle)
        glfwDestroyWindow(glfwWindowHandle)
        /* Remove error callback */
        glfwSetErrorCallback(null)?.free()
        /* Close window */
        glfwTerminate()
    }
}
