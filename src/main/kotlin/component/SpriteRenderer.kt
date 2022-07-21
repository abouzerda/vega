package component

import core.Component
import core.Transform
import org.joml.Vector4f

class SpriteRenderer(
    color: Vector4f = Vector4f(1f, 1f, 1f, 1f),
    sprite: Sprite = Sprite(null)
) : Component() {
    var syncedGPU = true
    var transform: Transform = Transform()
        set(value) {
            if (field != value) {
                field = value
                async()
            }
        }
    var color: Vector4f = color
        set(value) {
            if (field != value) {
                field = value
                async()
            }
        }
    var sprite = sprite
        set(value) {
            field = value
            async()
        }

    init {
        async()
    }

    override fun start() {
    }

    override fun update(dt: Float) {
        transform = gameObject.transform.deepCopy()
    }

    override fun imgui() {

    }

    private fun async() {
        syncedGPU = false
    }

    fun sync() {
        syncedGPU = true
    }
}