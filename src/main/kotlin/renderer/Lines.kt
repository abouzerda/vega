package renderer

import core.GLFWWindow
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import utils.Assets
import java.util.*

object Lines {
    private const val MAX_LINES = 500
    private val lines: MutableList<Line> = ArrayList<Line>()

    /* 6 floats per vertex, 2 vertices per line */
    private val vertexArray = FloatArray(MAX_LINES * 6 * 2)
    private val shader: Shader = Assets.loadShader("/debug.glsl")
    private var vaoID = 0
    private var vboID = 0
    private var started = false

    fun start() {
        /* Generate the vao */
        vaoID = GL30.glGenVertexArrays()
        GL30.glBindVertexArray(vaoID)
        /* Create the vbo and buffer some memory */
        vboID = GL15.glGenBuffers()
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID)
        GL15.glBufferData(
            GL15.GL_ARRAY_BUFFER,
            (vertexArray.size * java.lang.Float.BYTES).toLong(),
            GL15.GL_DYNAMIC_DRAW
        )
        /* Enable the vertex array attributes */
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 6 * java.lang.Float.BYTES, 0)
        GL20.glEnableVertexAttribArray(0)
        GL20.glVertexAttribPointer(
            1,
            3,
            GL11.GL_FLOAT,
            false,
            6 * java.lang.Float.BYTES,
            (3 * java.lang.Float.BYTES).toLong()
        )
        GL20.glEnableVertexAttribArray(1)
        GL11.glLineWidth(2.0f)
    }

    fun beginFrame() {
        if (!started) {
            start()
            started = true
        }
        /* Remove deadlines */
        var i = 0
        while (i < lines.size) {
            if (lines[i].beginFrame() < 0) {
                lines.removeAt(i)
                i--
            }
            i++
        }
    }

    fun draw() {
        if (lines.size <= 0) return
        var index = 0
        for (line in lines) {
            for (i in 0..1) {
                val position: Vector2f = if (i == 0) line.from else line.to
                val color: Vector3f = line.color
                /* Load position */
                vertexArray[index] = position.x
                vertexArray[index + 1] = position.y
                vertexArray[index + 2] = -10.0f
                /* Load the color */
                vertexArray[index + 3] = color.x
                vertexArray[index + 4] = color.y
                vertexArray[index + 5] = color.z
                index += 6
            }
        }
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID)
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, vertexArray.copyOfRange(0, lines.size * 6 * 2))
        /* Use our shader */
        shader.bind()
        shader.uploadMat4f("uProjection", GLFWWindow.currentScene.camera.projectionMatrix)
        shader.uploadMat4f("uView", GLFWWindow.currentScene.camera.viewMatrix)
        /* Bind the vao */
        GL30.glBindVertexArray(vaoID)
        GL20.glEnableVertexAttribArray(0)
        GL20.glEnableVertexAttribArray(1)
        /* Draw the batch */
        GL11.glDrawArrays(GL11.GL_LINES, 0, lines.size * 6 * 2)
        /* Disable Location */
        GL20.glDisableVertexAttribArray(0)
        GL20.glDisableVertexAttribArray(1)
        GL30.glBindVertexArray(0)
        /* Unbind shader */
        shader.unbind()
    }

    fun addLine(from: Vector2f, to: Vector2f, color: Vector3f = Vector3f(0f, 1f, 0f), lifetime: Int = 1) {
        if (lines.size >= MAX_LINES) return
        lines.add(Line(from, to, color, lifetime))
    }
}