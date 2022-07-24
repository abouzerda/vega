package component

import core.GLFWWindow
import core.GameObject
import io.MouseListener
import org.lwjgl.glfw.GLFW
import java.util.*

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
            holdingObject.get().transform.position.x = (MouseListener.cursorOrthoX - 16).toFloat()
            holdingObject.get().transform.position.y = (MouseListener.cursorOrthoY - 16).toFloat()
            if (MouseListener.pressedButton(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
                placeObject()
            }
        }
    }
}