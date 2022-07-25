package core

import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f


class Camera(var position: Vector2f) {
    val viewMatrix: Matrix4f = Matrix4f()
        get() {
            val cameraFront = Vector3f(0.0f, 0.0f, -1.0f)
            val cameraUp = Vector3f(0.0f, 1.0f, 0.0f)
            field.identity()
            field.lookAt(
                Vector3f(position.x, position.y, 20.0f), cameraFront.add(position.x, position.y, 0.0f), cameraUp
            )
            field.invert(inverseViewMatrix)
            return field
        }
    val inverseViewMatrix: Matrix4f = Matrix4f()
    val projectionMatrix: Matrix4f = Matrix4f()
    val inverseProjectionMatrix: Matrix4f = Matrix4f()
    val projectionSize = Vector2f(TILE_SIZE * GRID_WIDTH, TILE_SIZE * GRID_HEIGHT)


    init {
        adjustProjection()
    }

    private fun adjustProjection() {
        projectionMatrix.identity()
        projectionMatrix.ortho(
            0.0f, projectionSize.x, 0.0f, projectionSize.y, 0.0f, 100.0f
        )
        projectionMatrix.invert(inverseProjectionMatrix)
    }
}