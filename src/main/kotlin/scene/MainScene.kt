package scene

import core.Scene
import core.Shader
import org.lwjgl.opengl.GL33.*
import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.MemoryUtil.memFree
import utils.Utils


class MainScene : Scene() {
    val shader : Shader = Shader()
    var vertices = floatArrayOf(
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f,
        0.0f, 0.5f, 0.0f
    )
    var vaoId : Int = 0

    override fun init() {
        shader.createVertexShader(Utils.loadResource("/vertex.glsl"))
        shader.createFragmentShader(Utils.loadResource("/fragment.glsl"))
        shader.link()

        vaoId = glGenVertexArrays()
        glBindVertexArray(vaoId)
        val verticesBuffer = MemoryUtil.memAllocFloat(vertices.size)
        verticesBuffer.put(vertices).flip()
        val vboId = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vboId)
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)

        /* Clean up */
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindVertexArray(0)
        memFree(verticesBuffer)
    }

    override fun update(dt: Double) {
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        shader.bind()
        glBindVertexArray(vaoId)
        glEnableVertexAttribArray(0)
        glDrawArrays(GL_TRIANGLES, 0, 3)
        glDisableVertexAttribArray(0)
        glBindVertexArray(0)
        shader.unbind()
    }
}