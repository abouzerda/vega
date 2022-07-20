package scene

import component.Sprite
import component.SpriteRenderer
import core.*
import org.joml.Vector2f
import org.lwjgl.glfw.GLFW
import utils.Assets
import java.util.logging.Logger


class MainScene : Scene() {
    val logger: Logger = Logger.getLogger(javaClass.name)

    override fun init() {
        Assets.loadShader("/default.glsl")
        camera = Camera(Vector2f(-250f, 0f))
        val obj1 = GameObject("Object 1", Transform(Vector2f(200f, 100f), Vector2f(256f, 256f)), 2)
        obj1.addComponent<SpriteRenderer>(SpriteRenderer(sprite = Sprite(Assets.loadTexture("assets/images/blendImage1.png"))))
        this.addGameObject(obj1)
        val obj2 = GameObject("Object 2", Transform(Vector2f(400f, 100f), Vector2f(256f, 256f)), 3)
        obj2.addComponent<SpriteRenderer>(SpriteRenderer(sprite = Sprite(Assets.loadTexture("assets/images/blendImage2.png"))))
        this.addGameObject(obj2)
    }

    override fun update(dt: Float) {
        for (gameObject in this.gameObjects) {
            gameObject.update(dt)
        }
        this.renderer.render()
    }
}