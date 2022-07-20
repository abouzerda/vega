package component

import core.Component
import org.joml.Vector2f
import org.joml.Vector4f

class SpriteRenderer(
    var color: Vector4f = Vector4f(1f, 1f, 1f, 1f),
    var sprite: Sprite = Sprite(null)
) : Component() {
    override fun start() {
    }

    override fun update(dt: Float) {
    }
}