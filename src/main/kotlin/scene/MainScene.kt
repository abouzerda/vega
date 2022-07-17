package scene

import component.SpriteRenderer
import core.*
import org.joml.Vector2f
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL33.*
import utils.Assets


class MainScene : Scene() {
    override fun init() {
        camera = Camera(Vector2f(-250f, 0f))
        val obj1 = GameObject("Object 1", Transform(Vector2f(100f, 100f), Vector2f(256f, 256f)))
        obj1.addComponent<SpriteRenderer>(SpriteRenderer(texture = Assets.getTexture("assets/images/testImage.png")))
        this.addGameObject(obj1)
        Assets.getShader("/default.glsl")
    }

    override fun update(dt: Float) {
        if (KeyListener.pressedKey(GLFW.GLFW_KEY_UP)) camera.position.y -= dt * 50.0f
        if (KeyListener.pressedKey(GLFW.GLFW_KEY_DOWN)) camera.position.y += dt * 50.0f
        if (KeyListener.pressedKey(GLFW.GLFW_KEY_LEFT)) camera.position.x += dt * 50.0f
        if (KeyListener.pressedKey(GLFW.GLFW_KEY_RIGHT)) camera.position.x -= dt * 50.0f

        for (gameObject in this.gameObjects) {
            gameObject.update(dt)
        }

        glClearColor(0f, 0f, 0f, 0f)
        glClear(GL_COLOR_BUFFER_BIT)
        this.renderer.render()
    }
}