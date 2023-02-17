package renderer

import org.joml.Vector2f
import org.joml.Vector3f

class Line(val from: Vector2f, val to: Vector2f, val color: Vector3f, private var lifetime: Int) {
    fun beginFrame(): Int {
        lifetime--
        return lifetime
    }
}