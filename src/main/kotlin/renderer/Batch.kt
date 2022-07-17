package renderer

import component.SpriteRenderer
import core.GLFWWindow
import org.joml.Vector2f
import org.lwjgl.opengl.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.glBindVertexArray

class Batch(batchSize: Int = MAX_BATCH_SIZE) {
    private var sprites: MutableList<SpriteRenderer> = mutableListOf()
    private var textures: MutableList<Texture> = mutableListOf()
    private val texSlots: IntArray = IntArray(8) { it }
    private var vertices: FloatArray = FloatArray(batchSize * 4 * VERTEX_SIZE)
    private var vaoID = 0
    private var vboID = 0
    private var shader: Shader = Shader()

    init {
        shader.parse()
        shader.compile()
        shader.link()
    }

    fun start() {
        /* Generate and bind a Vertex Array Object */
        vaoID = GL30.glGenVertexArrays()
        glBindVertexArray(vaoID)
        /* Allocate space for vertices */
        vboID = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vboID)
        glBufferData(GL_ARRAY_BUFFER, vertices.size.toLong() * java.lang.Float.BYTES, GL_DYNAMIC_DRAW)
        /* Create and upload indices buffer */
        val eboID = glGenBuffers()
        val indices = generateIndices()
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW)
        /* Enable the buffer attribute pointers */
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET.toLong())
        glEnableVertexAttribArray(0)
        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET.toLong())
        glEnableVertexAttribArray(1)
        glVertexAttribPointer(2, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET.toLong())
        glEnableVertexAttribArray(2)
        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET.toLong())
        glEnableVertexAttribArray(3)
    }

    fun addSprite(sprite: SpriteRenderer) {
        sprites.add(sprite)
        if (sprite.texture != null && !textures.contains(sprite.texture)) textures.add(sprite.texture!!)
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
        for (i in textures.indices) {
            glActiveTexture(GL_TEXTURE0 + i + 1)
            textures[i].bind()
        }
        shader.uploadIntArray("uTextures", texSlots)

        glBindVertexArray(vaoID)
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)

        glDrawElements(GL_TRIANGLES, this.sprites.size * 6, GL_UNSIGNED_INT, 0)

        glDisableVertexAttribArray(0)
        glDisableVertexAttribArray(1)
        glBindVertexArray(0)

        for (i in textures.indices) textures[i].unbind()

        shader.unbind()
    }

    private fun loadVertexProperties(index: Int) {
        val sprite = sprites[index]
        /* Find offset within array (4 vertices per sprite) */
        var offset = index * 4 * VERTEX_SIZE
        val color = sprite.color
        val texCoords: List<Vector2f> = sprite.texCoords

        var texId = 0
        if (sprite.texture != null) {
            for (i in textures.indices) {
                if (textures[i] == sprite.texture) {
                    texId = i + 1
                    break
                }
            }
        }
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
            /* Load texture coordinates */
            vertices[offset + 6] = texCoords[i].x
            vertices[offset + 7] = texCoords[i].y
            /* Load texture id */
            vertices[offset + 8] = texId.toFloat()
            offset += VERTEX_SIZE
        }
    }

    private fun generateIndices(): IntArray {
        return IntArray(6 * MAX_BATCH_SIZE).apply {
            for (i in 1..MAX_BATCH_SIZE) loadElementIndices(this, i - 1)
        }
    }

    private fun loadElementIndices(elements: IntArray, index: Int) {
        val offsetArrayIndex = 6 * index
        val offset = 4 * index
        /* 3, 2, 0, 0, 2, 1        7, 6, 4, 4, 6, 5 */
        /* Triangle 1 */
        elements[offsetArrayIndex] = offset + 3
        elements[offsetArrayIndex + 1] = offset + 2
        elements[offsetArrayIndex + 2] = offset + 0
        /* Triangle 2 */
        elements[offsetArrayIndex + 3] = offset + 0
        elements[offsetArrayIndex + 4] = offset + 2
        elements[offsetArrayIndex + 5] = offset + 1
    }

    fun hasRoom(): Boolean = this.sprites.size < MAX_BATCH_SIZE
}
