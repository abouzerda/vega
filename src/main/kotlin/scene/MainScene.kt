package scene

import core.Camera
import core.Scene
import imgui.ImGui
import org.joml.Vector2f
import utils.Assets
import java.util.*
import java.util.logging.Logger


class MainScene : Scene() {
    val logger: Logger = Logger.getLogger(javaClass.name)

    override fun init() {
        Assets.loadShader("/default.glsl")
        Assets.loadTexture("assets/images/blendImage2.png")
        camera = Camera(Vector2f(-250f, 0f))
        load()
        activeGameObject = Optional.ofNullable(gameObjects.firstOrNull())
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