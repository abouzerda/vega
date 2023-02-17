package component.gizmo

import component.Component
import component.Sprite
import component.SpriteRenderer
import core.GLFWWindow
import core.GameObject
import core.Prefabs
import org.joml.Vector2f
import org.joml.Vector4f

class TranslateGizmo(arrowSprite: Sprite) : Component() {
    private val xAxisColor = Vector4f(1f, 0f, 0f, 1f)
    private val xAxisColorHover = Vector4f()
    private val yAxisColor = Vector4f(0f, 1f, 0f, 1f)
    private val yAxisColorHover = Vector4f()
    private val xAxisObject: GameObject
    private val yAxisObject: GameObject
    private val xAxisSprite: SpriteRenderer?
    private val yAxisSprite: SpriteRenderer?
    private var activeGameObject: GameObject? = null
    private val xAxisOffset = Vector2f(64f, -5f)
    private val yAxisOffset = Vector2f(16f, 61f)

    init {
        xAxisObject = Prefabs.generateGizmoObject(arrowSprite, 16f, 48f)
        yAxisObject = Prefabs.generateGizmoObject(arrowSprite, 16f, 48f)
        xAxisSprite = xAxisObject.getComponent(SpriteRenderer::class.java)
        yAxisSprite = yAxisObject.getComponent(SpriteRenderer::class.java)
        GLFWWindow.currentScene.addGameObject(xAxisObject)
        GLFWWindow.currentScene.addGameObject(yAxisObject)
    }

    override fun start() {
        xAxisObject.transform.rotation = 90f
        yAxisObject.transform.rotation = 180f
    }

    override fun update(dt: Float) {
        activeGameObject = GLFWWindow.currentScene.activeGameObject.orElse(null)
        if (activeGameObject != null) {
            setActive()
        } else {
            setInactive()
        }
        if (activeGameObject != null) {
            xAxisObject.transform.position.set(activeGameObject!!.transform.position)
            yAxisObject.transform.position.set(activeGameObject!!.transform.position)
            xAxisObject.transform.position.add(xAxisOffset)
            yAxisObject.transform.position.add(yAxisOffset)
        }
    }

    private fun setActive() {
        xAxisSprite!!.color = xAxisColor
        yAxisSprite!!.color = yAxisColor
    }

    private fun setInactive() {
        activeGameObject = null
        xAxisSprite!!.color = Vector4f(0f, 0f, 0f, 0f)
        yAxisSprite!!.color = Vector4f(0f, 0f, 0f, 0f)
    }
}