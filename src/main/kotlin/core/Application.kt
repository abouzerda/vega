package core

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.*

abstract class Application {
    var window : Window = Window()

    abstract fun init()
    abstract fun update(dt : Double)

    fun run() {
        init()
        var time = glfwGetTime()
        while (!glfwWindowShouldClose(Window.glfwWindow)) {
            glfwPollEvents()
            glClearColor(1.0f,1.0f,1.0f,1.0f)
            glClear(GL_COLOR_BUFFER_BIT)
            val dt = glfwGetTime() - time
            time = glfwGetTime()
            update(dt)
            glfwSwapBuffers(Window.glfwWindow)
        }
    }
}