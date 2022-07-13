package renderer

import core.COLOR_SIZE
import core.POSITION_SIZE
import core.UV_SIZE
import core.VERTEX_SIZE_BYTES
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL33.*
import org.lwjgl.system.MemoryUtil


class Mesh(vertices: FloatArray, elements: IntArray) {
    var vaoId : Int = glGenVertexArrays()
    var vboId : Int = glGenBuffers()
    var eboId : Int = glGenBuffers()

    init {
        val vertexBuffer = BufferUtils.createFloatBuffer(vertices.size).put(vertices).flip()
        val elementBuffer = BufferUtils.createIntBuffer(elements.size).put(elements).flip()
        /* Bind the vertex array object */
        glBindVertexArray(vaoId)
        /* Upload vertex buffer */
        glBindBuffer(GL_ARRAY_BUFFER, vboId)
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW)
        /* Upload element buffer */
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW)
        /* Add the vertex attribute pointers */
        glVertexAttribPointer(0, POSITION_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, 0)
        glEnableVertexAttribArray(0)
        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, (POSITION_SIZE * Float.SIZE_BYTES).toLong())
        glEnableVertexAttribArray(1)
        glVertexAttribPointer(2, UV_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, ((POSITION_SIZE + COLOR_SIZE) * Float.SIZE_BYTES).toLong())
        glEnableVertexAttribArray(2)
        /* Clean up */
        //glBindBuffer(GL_ARRAY_BUFFER, 0)
        //glBindVertexArray(0)
        //MemoryUtil.memFree(vertexBuffer)
    }
}