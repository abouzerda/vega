package component

import org.joml.Vector3f
import org.joml.Vector4f

class Test : Component() {
    private var colliderType: Int = 0
    private var friction: Float = 0.8f
    var velocity: Vector3f = Vector3f(0f, 0.5f, 0f)
    var isActive: Boolean = false

    @Transient
    var tmp: Vector4f = Vector4f(0f, 0f, 0f, 0f)

    override fun start() {}
    override fun update(dt: Float) {}
}