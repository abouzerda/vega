package scene

import component.CameraControls
import component.Grid
import component.MouseControls
import component.SpriteSheet
import core.GLFWWindow
import core.Scene
import io.MouseListener
import org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT
import utils.Assets
import java.util.*
import java.util.logging.Logger


class MainScene : Scene() {
    val logger: Logger = Logger.getLogger(javaClass.name)
    private val sprites: SpriteSheet
        get() = Assets.loadSpriteSheet("assets/images/frog.png")

    private val components = mutableListOf(MouseControls, Grid, CameraControls(this.camera))

    override fun init() {
        Assets.loadShader("/default.glsl")
        Assets.loadShader("/debug.glsl")
        Assets.loadSpriteSheet("assets/images/spriteSheet.png", 16, 16, 26, 0)
        Assets.loadSpriteSheet("assets/images/frog.png", 32, 32, 12, 0)
    }

    override fun update(dt: Float) {
        if (MouseListener.pressedButton(GLFW_MOUSE_BUTTON_LEFT)) {
            val gameObjectId: Int = GLFWWindow.objectIdMask.getObjectId(
                MouseListener.screenX.toInt(),
                MouseListener.screenY.toInt()
            )
            activeGameObject = Optional.ofNullable(gameObjects.find { it.id == gameObjectId })
        }
        camera.adjustProjection()
        components.forEach { it.update(dt) }
        for (gameObject in this.gameObjects) {
            gameObject.update(dt)
        }
    }

    override fun render() {
        this.renderer.render()
    }

    override fun imgui() {
        Widgets.showSprites(sprites)
        Widgets.showMouse()
        Widgets.showInspector()
    }
}