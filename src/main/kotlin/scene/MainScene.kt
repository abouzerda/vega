package scene

import component.SpriteRenderer
import core.*
import org.joml.Vector2f
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL33.*
import renderer.Mesh
import renderer.Shader
import renderer.Texture
import utils.Utils


class MainScene : Scene() {
    lateinit var shader : Shader
    lateinit var mesh : Mesh
    lateinit var texture : Texture
    private val vertices = floatArrayOf(
        /* x,y,z */         /* r,g,b,a */               /* x,y */
        100f, 0f, 0.0f,     1.0f, 0.0f, 0.0f, 1.0f,     1f, 1f,
        0f, 100f, 0.0f,     0.0f, 1.0f, 0.0f, 1.0f,     0f, 0f,
        100f, 100f, 0.0f,   1.0f, 0.0f, 1.0f, 1.0f,     1f, 0f,
        0f, 0f, 0.0f,       1.0f, 1.0f, 0.0f, 1.0f,     0f, 1f
    )
    private val elements = intArrayOf(
        2, 1, 0,
        0, 1, 3
    )
    private val gameObj = GameObject("")

    override fun init() {
        gameObj.addComponent<SpriteRenderer>(SpriteRenderer())
        addGameObject(gameObj)


        camera = Camera(Vector2f())
        shader = Shader()
        shader.compile(
            vertexShaderCode = Utils.loadResource("/vertex.glsl"),
            fragmentShaderCode = Utils.loadResource("/fragment.glsl")
        )
        shader.link()
        texture = Texture("assets/images/testImage.png")
        mesh = Mesh(vertices, elements)
    }

    override fun update(dt: Float) {
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f)

        if(KeyListener.pressedKey(GLFW.GLFW_KEY_UP)) camera.position.y -= dt * 50.0f
        if(KeyListener.pressedKey(GLFW.GLFW_KEY_DOWN)) camera.position.y += dt * 50.0f
        if(KeyListener.pressedKey(GLFW.GLFW_KEY_LEFT)) camera.position.x += dt * 50.0f
        if(KeyListener.pressedKey(GLFW.GLFW_KEY_RIGHT)) camera.position.x -= dt * 50.0f

        shader.bind()

        shader.uploadTexture("TEX_SAMPLER", 0)
        glActiveTexture(GL_TEXTURE0)
        texture.bind()

        shader.uploadMat4f("uProjection", camera.getProjectionMatrix())
        shader.uploadMat4f("uView", camera.getViewMatrix())
        glBindVertexArray(mesh.vaoId)
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)
        glDrawElements(GL_TRIANGLES, elements.size, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0)
        glDisableVertexAttribArray(1)
        glBindVertexArray(0)
        shader.unbind()
    }
}