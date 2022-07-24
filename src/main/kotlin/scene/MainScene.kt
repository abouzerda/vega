package scene

import component.MouseControls
import component.SpriteSheet
import core.Camera
import core.Scene
import imgui.ImGui
import org.joml.Vector2f
import utils.Assets
import java.util.*
import java.util.logging.Logger


class MainScene : Scene() {
    val logger: Logger = Logger.getLogger(javaClass.name)
    private val sprites: SpriteSheet
        get() = Assets.loadSpriteSheet("assets/images/spriteSheet.png")


    override fun init() {
        Assets.loadShader("/default.glsl")
        Assets.loadTexture("assets/images/blendImage2.png")
        Assets.loadSpriteSheet("assets/images/spriteSheet.png", 16, 16, 26, 0)

        camera = Camera(Vector2f(-250f, 0f))
        activeGameObject = Optional.ofNullable(gameObjects.firstOrNull())
    }

    override fun update(dt: Float) {
        MouseControls.update(dt)
        for (gameObject in this.gameObjects) {
            gameObject.update(dt)
        }
        this.renderer.render()
    }

    override fun imgui() {
        Widgets.showSprites(sprites)
        Widgets.showMouse()
        activeGameObject.ifPresent {
            ImGui.begin("Inspector")
            it.imgui()
            ImGui.end()
        }
    }
}