package renderer

import org.lwjgl.opengl.GL30.*

class ObjectIdMask(width: Int, height: Int) {
    private var pickingTextureId = 0
    private var fbo = 0
    private var depthTexture = 0

    init {
        if (!init(width, height)) {
            assert(false) { "Error initializing object id mask" }
        }
    }

    fun init(width: Int, height: Int): Boolean {
        // Generate framebuffer
        fbo = glGenFramebuffers()
        glBindFramebuffer(GL_FRAMEBUFFER, fbo)

        // Create the texture to render the data to, and attach it to our framebuffer
        pickingTextureId = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, pickingTextureId)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexImage2D(
            GL_TEXTURE_2D, 0, GL_RGB32F, width, height, 0,
            GL_RGB, GL_FLOAT, 0
        )
        glFramebufferTexture2D(
            GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D,
            pickingTextureId, 0
        )

        // Create the texture object for the depth buffer
        glEnable(GL_TEXTURE_2D)
        depthTexture = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, depthTexture)
        glTexImage2D(
            GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, width, height, 0,
            GL_DEPTH_COMPONENT, GL_FLOAT, 0
        )
        glFramebufferTexture2D(
            GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT,
            GL_TEXTURE_2D, depthTexture, 0
        )

        // Disable the reading
        glReadBuffer(GL_NONE)
        glDrawBuffer(GL_COLOR_ATTACHMENT0)
        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            assert(false) { "Error: Framebuffer is not complete" }
            return false
        }

        // Unbind the texture and framebuffer
        glBindTexture(GL_TEXTURE_2D, 0)
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
        return true
    }

    fun enableWriting() {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, fbo)
    }

    fun disableWriting() {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0)
    }

    fun readPixel(x: Int, y: Int): Int {
        glBindFramebuffer(GL_READ_FRAMEBUFFER, fbo)
        glReadBuffer(GL_COLOR_ATTACHMENT0)
        val pixels = FloatArray(3)
        glReadPixels(x, y, 1, 1, GL_RGB, GL_FLOAT, pixels)
        return pixels[0].toInt() - 1
    }
}