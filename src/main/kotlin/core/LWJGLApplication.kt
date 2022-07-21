package core

import org.lwjgl.Version
import org.lwjgl.glfw.GLFW
import java.util.logging.Logger

open class LWJGLApplication {
    private val logger: Logger = Logger.getLogger(javaClass.name)

    init {
        this.logger.info("LWJGL ${Version.getVersion()}!")
        GLFWWindow.init()
    }

    fun run() {
        var dt: Float? = null
        while (!GLFWWindow.shouldClose()) {
            val time = GLFW.glfwGetTime()
            GLFWWindow.update(dt)
            dt = (GLFW.glfwGetTime() - time).toFloat()
        }
        /* If reached this point window should close */
        GLFWWindow.close()
    }

    fun showScene(scene: Scene) {
        GLFWWindow.showScene(scene.apply {
            load()
            init()
            start()
        })
    }
}