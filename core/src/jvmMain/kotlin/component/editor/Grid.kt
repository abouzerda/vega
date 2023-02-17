package component.editor

import component.Component
import core.Camera
import core.GLFWWindow
import org.joml.Vector2f
import org.joml.Vector3f
import renderer.Debug
import utils.Settings
import kotlin.math.max

object Grid : Component() {

    override fun start() {}

    override fun update(dt: Float) {
        val camera: Camera = GLFWWindow.currentScene.camera
        val cameraPos: Vector2f = camera.position
        val projectionSize: Vector2f = camera.projectionSize

        val firstX: Int = ((cameraPos.x / Settings.GRID_WIDTH).toInt() - 1) * Settings.GRID_HEIGHT
        val firstY: Int = ((cameraPos.y / Settings.GRID_HEIGHT).toInt() - 1) * Settings.GRID_HEIGHT

        val numVtLines = (projectionSize.x * camera.zoom / Settings.GRID_WIDTH).toInt() + 2
        val numHzLines = (projectionSize.y * camera.zoom / Settings.GRID_HEIGHT).toInt() + 2

        val width: Int = (projectionSize.x * camera.zoom).toInt() + Settings.GRID_WIDTH * 2
        val height: Int = (projectionSize.y * camera.zoom).toInt() + Settings.GRID_HEIGHT * 2

        val maxLines = max(numVtLines, numHzLines)
        val color = Vector3f(0.2f, 0.2f, 0.2f)

        for (i in 0 until maxLines) {
            val x: Int = firstX + (Settings.GRID_WIDTH * i)
            val y: Int = firstY + (Settings.GRID_HEIGHT * i)
            if (i < numVtLines) {
                Debug.addLine(
                    from = Vector2f(x.toFloat(), firstY.toFloat()),
                    to = Vector2f(x.toFloat(), (firstY + height).toFloat()),
                    color = color
                )
            }
            if (i < numHzLines) {
                Debug.addLine(
                    from = Vector2f(firstX.toFloat(), y.toFloat()),
                    to = Vector2f((firstX + width).toFloat(), y.toFloat()),
                    color = color
                )
            }
        }
    }
}