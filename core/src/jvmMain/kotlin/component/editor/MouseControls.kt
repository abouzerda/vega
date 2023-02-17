package component.editor

import component.Component
import core.GLFWWindow
import core.GameObject
import io.MouseListener
import org.lwjgl.glfw.GLFW
import utils.Settings
import java.util.*
import kotlin.math.floor

object MouseControls : Component() {
    private var holdingObject: Optional<GameObject> = Optional.empty()

    fun pickupObject(gameObject: GameObject) {
        holdingObject = Optional.of(gameObject)
        if (!GLFWWindow.currentScene.gameObjects.contains(gameObject)) {
            GLFWWindow.currentScene.addGameObject(gameObject)
        }
    }

    private fun placeObject() {
        holdingObject = Optional.empty()
    }

    override fun start() {}

    override fun update(dt: Float) {
        if (holdingObject.isPresent) {

            holdingObject.get().transform.position.x =
                (floor(MouseListener.cursorOrthoX / Settings.GRID_WIDTH) * Settings.GRID_WIDTH).toFloat()
            holdingObject.get().transform.position.y =
                (floor(MouseListener.cursorOrthoY / Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT).toFloat()

            if (MouseListener.pressedButton(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
                placeObject()
            }
        }
    }
}