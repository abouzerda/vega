package core

import io.ImGuiAdapter
import io.KeyListener
import io.MouseListener
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL14.glBlendFuncSeparate
import org.lwjgl.system.MemoryUtil.NULL
import renderer.*
import scene.MainScene
import utils.Assets
import java.util.logging.Logger

object GLFWWindow {
    private val logger: Logger = Logger.getLogger(javaClass.name)

    var width = 1920
    var height = 1080
    private const val title = ""

    private var glfwWindowHandle = 0L
    private lateinit var imGui: ImGuiAdapter
    lateinit var frameBuffer: FrameBuffer
    lateinit var objectIdMask: ObjectIdMask

    internal var currentScene: Scene = MainScene()

    const val targetAspectRatio: Float = 16.0f / 9.0f

    fun showScene(scene: Scene) {
        this.currentScene = scene
    }

    internal fun shouldClose(): Boolean = glfwWindowShouldClose(glfwWindowHandle)

    internal fun init() {
        this.logger.info("GLFW ${glfwGetVersionString().split(' ').first()}!")
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
        this.logger.info("OpenGL ${glGetString(GL_VERSION)?.split(' ')?.first()}!")
        /* Enable Alpha blending */
        glEnable(GL_BLEND)
        glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE)
        frameBuffer = FrameBuffer(1600, 900)
        objectIdMask = ObjectIdMask(1600, 900)
        glViewport(0, 0, 1600, 900)


        imGui = ImGuiAdapter(glfwWindowHandle)
        imGui.init()
    }

    internal fun update(dt: Float) {

        val defaultShader: Shader = Assets.loadShader("/default.glsl")
        val pickingShader: Shader = Assets.loadShader("/mask.glsl")

        with(this.objectIdMask) {
            glDisable(GL_BLEND)
            enableWriting()
            /* Set up the viewport */
            glViewport(0, 0, 1600, 900)
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
            /* Change the current shader */
            Renderer.currentShader = pickingShader
            currentScene.render()
            disableWriting()
            glEnable(GL_BLEND)
        }


        /* Poll events */
        glfwPollEvents()
        with(this.frameBuffer) {
            bind()
            Debug.beginFrame()
            /* Clear screen */
            glClearColor(1f, 1f, 1f, 1f)
            glClear(GL_COLOR_BUFFER_BIT)
            /* Update current scene */
            Debug.draw()
            Renderer.currentShader = defaultShader
            currentScene.update(dt)
            currentScene.render()
            unbind()
        }
        this.imGui.update(dt)
        glfwSwapBuffers(glfwWindowHandle)
        MouseListener.endFrame()
    }

    internal fun close() {
        currentScene.save()
        /* Clean up memory */
        glfwFreeCallbacks(glfwWindowHandle)
        glfwDestroyWindow(glfwWindowHandle)
        /* Remove error callback */
        glfwSetErrorCallback(null)?.free()
        /* Close window */
        glfwTerminate()
    }
}
