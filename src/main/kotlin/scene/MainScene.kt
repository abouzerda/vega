package scene

import component.Sprite
import component.SpriteRenderer
import core.*
import imgui.ImGui
import org.joml.Vector2f
import org.joml.Vector4f
import utils.Assets
import java.util.*
import java.util.logging.Logger


class MainScene : Scene() {
    val logger: Logger = Logger.getLogger(javaClass.name)

    override fun init() {
        Assets.loadShader("/default.glsl")
        camera = Camera(Vector2f(-250f, 0f))
        val obj1 = GameObject("Object 1", Transform(Vector2f(200f, 100f), Vector2f(256f, 256f)), 2)
        obj1.addComponent<SpriteRenderer>(SpriteRenderer(color = Vector4f(1f, 0f, 0f, 1f)))
        this.addGameObject(obj1)
        val obj2 = GameObject("Object 2", Transform(Vector2f(400f, 100f), Vector2f(256f, 256f)), 3)
        obj2.addComponent<SpriteRenderer>(SpriteRenderer(sprite = Sprite(Assets.loadTexture("assets/images/blendImage2.png"))))
        this.addGameObject(obj2)

        this.activeGameObject = Optional.of(obj1)
    }

    override fun update(dt: Float) {
        for (gameObject in this.gameObjects) {
            gameObject.update(dt)
        }
        this.renderer.render()
    }

    override fun imgui() {
        ImGui.begin("Test Window")
        ImGui.text("Some random Text")
        ImGui.end()
    }
}