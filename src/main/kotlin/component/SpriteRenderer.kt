package component

import core.Component
import org.joml.Vector2f
import org.joml.Vector4f
import renderer.Texture

class SpriteRenderer(
    var color: Vector4f = Vector4f(1f, 1f, 1f, 1f),
    var texture: Texture? = null
) : Component() {
    val texCoords: List<Vector2f> = listOf(
        Vector2f(1f, 1f),
        Vector2f(1f, 0f),
        Vector2f(0f, 0f),
        Vector2f(0f, 1f)
    )

    override fun start() {
    }

    override fun update(dt: Float) {
    }
}