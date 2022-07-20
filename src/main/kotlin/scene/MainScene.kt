package scene

import component.SpriteRenderer
import core.*
import org.joml.Vector2f
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL33.*
import utils.Assets
import java.util.logging.Logger


class MainScene : Scene() {
    val logger: Logger = Logger.getLogger(javaClass.name)

    override fun init() {
        Assets.loadShader("/default.glsl")
        val spriteSheet = Assets.loadSpriteSheet("assets/images/spriteSheet.png", 16, 16, 26, 0)
        camera = Camera(Vector2f(-250f, 0f))
        val obj1 = GameObject("Object 1", Transform(Vector2f(100f, 100f), Vector2f(256f, 256f)))
        obj1.addComponent<SpriteRenderer>(SpriteRenderer(sprite = spriteSheet.sprites[15]))
        this.addGameObject(obj1)
    }

    override fun update(dt: Float) {
        if (KeyListener.pressedKey(GLFW.GLFW_KEY_W)) camera.position.y -= dt * 50.0f
        if (KeyListener.pressedKey(GLFW.GLFW_KEY_S)) camera.position.y += dt * 50.0f
        if (KeyListener.pressedKey(GLFW.GLFW_KEY_A)) camera.position.x += dt * 50.0f
        if (KeyListener.pressedKey(GLFW.GLFW_KEY_D)) camera.position.x -= dt * 50.0f

        for (gameObject in this.gameObjects) {
            gameObject.update(dt)
        }

        glClearColor(0f, 0f, 0f, 0f)
        glClear(GL_COLOR_BUFFER_BIT)
        this.renderer.render()
    }
}