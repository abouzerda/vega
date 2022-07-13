package core

import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f




class Camera(var position: Vector2f) {

    private var viewMatrix: Matrix4f = Matrix4f()
    private val projectionMatrix: Matrix4f = Matrix4f()

    init {
        adjustProjection()
    }

    private fun adjustProjection() {
        projectionMatrix.identity()
        projectionMatrix.ortho(
            0.0f, GRID_WIDTH * TILE_SIZE,
            0.0f, GRID_HEIGHT * TILE_SIZE,
            0.0f, 100.0f
        )
    }

    fun getViewMatrix(): Matrix4f {
        val cameraFront = Vector3f(0.0f, 0.0f, -1.0f)
        val cameraUp = Vector3f(0.0f, 1.0f, 0.0f)
        viewMatrix.identity()
        viewMatrix.lookAt(
            Vector3f(position.x, position.y, 20.0f),
            cameraFront.add(position.x, position.y, 0.0f),
            cameraUp
        )
        return viewMatrix
    }

    fun getProjectionMatrix(): Matrix4f {
        return projectionMatrix
    }
}