package component

import core.Component
import core.Transform
import imgui.ImGui
import org.joml.Vector4f

class SpriteRenderer(
    color: Vector4f = Vector4f(1f, 1f, 1f, 1f),
    sprite: Sprite = Sprite()
) : Component() {
    @Transient
    var syncedGPU = false

    @Transient
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
        val colors = FloatArray(4) { color[it] }
        if (ImGui.colorPicker4("", colors)) {
            this.color.set(colors)
            async()
        }
    }

    private fun async() {
        syncedGPU = false
    }

    fun sync() {
        syncedGPU = true
    }
}