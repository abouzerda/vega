package core

import org.joml.Vector2f

class Transform(var position: Vector2f = Vector2f(), var scale: Vector2f = Vector2f()) {
    fun deepCopy(): Transform = Transform(Vector2f(position), Vector2f(scale))
    override fun equals(other: Any?): Boolean = other is Transform && position == other.position && scale == other.scale
    override fun hashCode(): Int = 31 * position.hashCode() + scale.hashCode()
}