package renderer

import component.SpriteRenderer
import core.GLFWWindow
import org.lwjgl.opengl.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.glDisableVertexAttribArray
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL30.glBindVertexArray
import utils.Utils

class Batch(batchSize: Int = MAX_BATCH_SIZE) {
    private var sprites: MutableList<SpriteRenderer> = mutableListOf()
    private var vertices: FloatArray = FloatArray(batchSize * 4 * VERTEX_SIZE)
    private var vaoID = 0
    private var vboID = 0
    private var shader: Shader = Shader()

    init {
        shader.compile(
            Utils.loadResource("/vertex.glsl"),
            Utils.loadResource("/fragment.glsl"),
        )
        shader.link()
    }

    fun start() {
        /* Generate and bind a Vertex Array Object */
        vaoID = GL30.glGenVertexArrays()
        GL30.glBindVertexArray(vaoID)
        /* Allocate space for vertices */
        vboID = GL15.glGenBuffers()
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID)
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices.size.toLong() * java.lang.Float.BYTES, GL15.GL_DYNAMIC_DRAW)
        /* Create and upload indices buffer */
        val eboID = GL15.glGenBuffers()
        val indices = generateIndices()
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, eboID)
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW)
        /* Enable the buffer attribute pointers */
        GL20C.glVertexAttribPointer(0, POS_SIZE, GL11.GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET.toLong())
        GL20.glEnableVertexAttribArray(0)
        GL20C.glVertexAttribPointer(1, COLOR_SIZE, GL11.GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET.toLong())
        GL20.glEnableVertexAttribArray(1)
    }

    fun addSprite(sprite: SpriteRenderer) {
        sprites.add(sprite)
        loadVertexProperties(sprites.size - 1)
    }

    fun render() {
        /* For now, we will rebuffer all data every frame */
        glBindBuffer(GL_ARRAY_BUFFER, vboID)
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices)

        /* Use shader */
        shader.bind()
        shader.uploadMat4f("uProjection", GLFWWindow.currentScene.camera.getProjectionMatrix())
        shader.uploadMat4f("uView", GLFWWindow.currentScene.camera.getViewMatrix())

        glBindVertexArray(vaoID)
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)

        glDrawElements(GL_TRIANGLES, this.sprites.size * 6, GL_UNSIGNED_INT, 0)

        glDisableVertexAttribArray(0)
        glDisableVertexAttribArray(1)
        glBindVertexArray(0)

        shader.unbind()
    }

    private fun loadVertexProperties(index: Int) {
        val sprite = sprites[index]
        /* Find offset within array (4 vertices per sprite) */
        var offset = index * 4 * VERTEX_SIZE
        val color = sprite.color
        /* Add vertices with the appropriate properties */
        var xAdd = 1.0f
        var yAdd = 1.0f
        for (i in 0..3) {
            if (i == 1) {
                yAdd = 0.0f
            } else if (i == 2) {
                xAdd = 0.0f
            } else if (i == 3) {
                yAdd = 1.0f
            }
            /* Load position */
            vertices[offset] =
                sprite.gameObject?.transform?.position?.x!! + xAdd * sprite.gameObject?.transform?.scale?.x!!
            vertices[offset + 1] =
                sprite.gameObject?.transform?.position?.y!! + yAdd * sprite.gameObject?.transform?.scale?.y!!
            /* Load color */
            vertices[offset + 2] = color.x
            vertices[offset + 3] = color.y
            vertices[offset + 4] = color.z
            vertices[offset + 5] = color.w
            offset += VERTEX_SIZE
        }
    }

    private fun generateIndices(): IntArray {
        return IntArray(6 * MAX_BATCH_SIZE).apply {
            for (i in 1..MAX_BATCH_SIZE) loadElementIndices(this, i - 1)
        }
    }

    private fun loadElementIndices(elements: IntArray, index: Int) {
        val offsetArrayIndex = 6 * index;
        val offset = 4 * index;
        /* 3, 2, 0, 0, 2, 1        7, 6, 4, 4, 6, 5 */
        /* Triangle 1 */
        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset + 0;
        /* Triangle 2 */
        elements[offsetArrayIndex + 3] = offset + 0;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;
    }

    fun hasRoom(): Boolean = this.sprites.size < MAX_BATCH_SIZE
}
