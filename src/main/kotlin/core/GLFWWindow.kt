package core

import io.ImGUI
import io.KeyListener
import io.MouseListener
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL
import scene.MainScene
import java.util.logging.Logger

object GLFWWindow {
    private val logger: Logger = Logger.getLogger(javaClass.name)

    var width = 1920
    var height = 1080
    private const val title = ""

    private var glfwWindowHandle = 0L
    private lateinit var imGui: ImGUI

    internal var currentScene: Scene = MainScene()

    fun showScene(scene: Scene) {
        this.currentScene = scene
    }

    internal fun shouldClose(): Boolean = glfwWindowShouldClose(glfwWindowHandle)

    internal fun init() {
        this.logger.info("GLFW ${glfwGetVersionString()}!")
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
        glfwSetKeyCallback(glfwWindowHandle, KeyListener::sceneKeyCallback)
        /* Set up mouse input callbacks */
        glfwSetMouseButtonCallback(glfwWindowHandle, MouseListener::sceneMouseButtonCallback)
        glfwSetCursorPosCallback(glfwWindowHandle, MouseListener::sceneCursorPositionCallback)
        glfwSetCursorEnterCallback(glfwWindowHandle, MouseListener::sceneCursorEnterCallback)
        glfwSetScrollCallback(glfwWindowHandle, MouseListener::sceneScrollCallback)
        glfwSetWindowSizeCallback(glfwWindowHandle) { _: Long, newWidth: Int, newHeight: Int ->
            width = newWidth
            height = newHeight
        }
        /* Make OpenGL the current context */
        glfwMakeContextCurrent(glfwWindowHandle)
        /* Enable V-Sync */
        glfwSwapInterval(1)

        GL.createCapabilities()
        this.logger.info("OpenGL ${glGetString(GL_VERSION)}!")

        /* Enable Alpha blending */
        glEnable(GL_BLEND)
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA)

        imGui = ImGUI(glfwWindowHandle)
        imGui.init()
    }

    internal fun update(dt: Float?) {
        /* Poll events */
        glfwPollEvents()
        /* Clear screen */
        glClearColor(1f, 1f, 1f, 1f)
        glClear(GL_COLOR_BUFFER_BIT)
        /* Update current scene */
        if (dt != null) {
            this.currentScene.update(dt)
            this.imGui.update(dt)
        }
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
