package renderer

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.*
import org.lwjgl.stb.STBImage.*


class Texture(filepath: String) {
    private val texID: Int = glGenTextures()
    val width: Int
    val height: Int

    init {
        glBindTexture(GL_TEXTURE_2D, texID)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
        val width = BufferUtils.createIntBuffer(1)
        val height = BufferUtils.createIntBuffer(1)
        val channels = BufferUtils.createIntBuffer(1)
        stbi_set_flip_vertically_on_load(true)
        val image = stbi_load(filepath, width, height, channels, 0)
        if (image != null) {
            this.width = width.get(0)
            this.height = height.get(0)
            val mode = when (channels.get(0)) {
                3 -> GL_RGB
                4 -> GL_RGBA
                else -> throw Exception("ERROR: Unknown number of channels")
            }
            glTexImage2D(
                GL_TEXTURE_2D, 0, mode,
                width.get(0), height.get(0),
                0, mode, GL_UNSIGNED_BYTE,
                image
            )
        } else {
            throw Exception("ERROR: Loading texture")
        }
        stbi_image_free(image)
    }

    fun bind() {
        glBindTexture(GL_TEXTURE_2D, texID)
    }

    fun unbind() {
        glBindTexture(GL_TEXTURE_2D, 0)
    }
}