package scene

import component.SpriteRenderer
import core.*
import org.joml.Vector2f
import org.joml.Vector4f
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL33.*
import renderer.Shader
import utils.Utils


class MainScene : Scene() {
    override fun init() {
        camera = Camera(Vector2f(-250f, 0f))

        val xOffset = 10
        val yOffset = 10

        val totalWidth = (600 - xOffset * 2).toFloat()
        val totalHeight = (300 - yOffset * 2).toFloat()
        val sizeX = totalWidth / 100.0f
        val sizeY = totalHeight / 100.0f
        val padding = 3f

        for (x in 0..99) {
            for (y in 0..99) {
                val xPos = xOffset + x * sizeX + padding * x
                val yPos = yOffset + y * sizeY + padding * y
                val go = GameObject("Obj$x$y", Transform(Vector2f(xPos, yPos), Vector2f(sizeX, sizeY)))
                go.addComponent<SpriteRenderer>(SpriteRenderer(Vector4f(xPos / totalWidth, yPos / totalHeight, 1f, 1f)))
                this.addGameObject(go)
            }
        }

        val shader = Shader()
        shader.compile(
            vertexShaderCode = Utils.loadResource("/vertex.glsl"),
            fragmentShaderCode = Utils.loadResource("/fragment.glsl")
        )
        shader.link()
    }

    override fun update(dt: Float) {
        println("${1 / dt} FPS")
        if (KeyListener.pressedKey(GLFW.GLFW_KEY_UP)) camera.position.y -= dt * 50.0f
        if (KeyListener.pressedKey(GLFW.GLFW_KEY_DOWN)) camera.position.y += dt * 50.0f
        if (KeyListener.pressedKey(GLFW.GLFW_KEY_LEFT)) camera.position.x += dt * 50.0f
        if (KeyListener.pressedKey(GLFW.GLFW_KEY_RIGHT)) camera.position.x -= dt * 50.0f

        for (gameObject in this.gameObjects) {
            gameObject.update(dt)
        }

        glClearColor(0f,0f,0f,0f)
        glClear(GL_COLOR_BUFFER_BIT)
        this.renderer.render()
    }
}