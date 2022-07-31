package scene

import component.SpriteSheet
import component.editor.CameraControls
import component.editor.Grid
import component.editor.MouseControls
import component.editor.NonPickable
import component.gizmo.TranslateGizmo
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

    init {
        onMousePress = { event ->
            when (event.button) {
                GLFW_MOUSE_BUTTON_LEFT -> {
                    val gameObjectId: Int = GLFWWindow.objectIdMask.getObjectId(
                        MouseListener.screenX.toInt(), MouseListener.screenY.toInt()
                    )
                    activeGameObject = Optional.ofNullable(
                        gameObjects.filter { !it.hasComponent<NonPickable>() }.find { it.id == gameObjectId }
                    )
                }
            }
        }
    }

    override fun init() {
        Assets.loadShader("/default.glsl")
        Assets.loadShader("/debug.glsl")
        Assets.loadSpriteSheet("assets/images/frog.png", 32, 32, 12, 0)
        val gizmos = Assets.loadSpriteSheet("assets/images/gizmos.png", 24, 48, 2, 0)
        components.add(TranslateGizmo(gizmos.sprites[1]))
        components.forEach { it.start() }
    }

    override fun update(dt: Float) {
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