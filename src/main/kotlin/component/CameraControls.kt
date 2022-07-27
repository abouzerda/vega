package component

import core.Camera
import core.DELTA
import io.KeyListener
import io.MouseListener
import org.joml.Vector2f
import org.lwjgl.glfw.GLFW
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sign

class CameraControls(private val camera: Camera) : Component() {

    private var dragDebounce = 0.032f
    private var clickOrigin: Vector2f = Vector2f()
    private var reset = false
    private var lerpTime = 0.0f
    private val dragSensitivity = 30.0f
    private val scrollSensitivity = 0.1f

    override fun start() {}

    override fun update(dt: Float) {
        if (MouseListener.pressedButton(GLFW.GLFW_MOUSE_BUTTON_LEFT) && dragDebounce > 0) {
            clickOrigin = Vector2f(MouseListener.cursorOrthoX.toFloat(), MouseListener.cursorOrthoY.toFloat())
            dragDebounce -= dt
            return
        } else if (MouseListener.pressedButton(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            val mousePos = Vector2f(MouseListener.cursorOrthoX.toFloat(), MouseListener.cursorOrthoY.toFloat())
            val delta: Vector2f = Vector2f(mousePos).sub(clickOrigin)
            camera.position.sub(delta.mul(dt).mul(dragSensitivity))
            clickOrigin.lerp(mousePos, dt)
        }
        if (dragDebounce <= 0.0f && !MouseListener.pressedButton(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            dragDebounce = 0.1f
        }
        if (MouseListener.cursorScrollY != 0.0) {
            var addValue =
                abs(MouseListener.cursorScrollY * scrollSensitivity).pow((1 / camera.zoom).toDouble()).toFloat()
            addValue *= -sign(MouseListener.cursorScrollY).toFloat()
            camera.zoom += addValue
        }
        if (KeyListener.pressedKey(GLFW.GLFW_KEY_R)) {
            reset = true
        }
        if (reset) {
            camera.position.lerp(Vector2f(), lerpTime)
            camera.zoom = camera.zoom + (1.0f - camera.zoom) * lerpTime
            lerpTime += 0.1f * dt
            if (abs(camera.position.x) <= 0.1f &&
                abs(camera.position.y) <= 0.1f &&
                abs(camera.zoom - 1.0f) <= DELTA
            ) {
                lerpTime = 0.0f
                camera.position.set(0f, 0f)
                camera.zoom = 1.0f
                reset = false
            }
        }
    }
}