package component

import core.Component
import org.joml.Vector4f
import renderer.Texture

class SpriteRenderer(
    var color: Vector4f = Vector4f(1f, 1f, 1f, 1f),
    var texture: Texture? = null
) : Component() {
    override fun start() {
    }

    override fun update(dt: Float) {
    }
}