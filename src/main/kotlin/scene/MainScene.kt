package scene

import component.Sprite
import component.SpriteRenderer
import core.*
import org.joml.Vector2f
import org.lwjgl.glfw.GLFW
import utils.Assets
import java.util.logging.Logger


class MainScene : Scene() {
    val logger: Logger = Logger.getLogger(javaClass.name)

    init {
        onKeyPress = {
            val spriteRenderer = gameObjects[0].getComponent(SpriteRenderer::class.java)
            val spriteSheet = Assets.loadSpriteSheet("assets/images/spriteSheet.png", 16, 16, 26, 0)
            val index = spriteSheet.sprites.indexOf(spriteRenderer?.sprite)
            when (it.key) {
                GLFW.GLFW_KEY_LEFT -> spriteRenderer?.sprite =
                    spriteSheet.sprites[kotlin.math.abs(index - 1 % spriteSheet.sprites.size)]
                GLFW.GLFW_KEY_RIGHT -> spriteRenderer?.sprite =
                    spriteSheet.sprites[kotlin.math.abs(index + 1 % spriteSheet.sprites.size)]
            }
        }
    }

    override fun init() {
        Assets.loadShader("/default.glsl")
        val spriteSheet = Assets.loadSpriteSheet("assets/images/spriteSheet.png", 16, 16, 26, 0)
        sprites = spriteSheet.sprites
        camera = Camera(Vector2f(-250f, 0f))
        val obj1 = GameObject("Object 1", Transform(Vector2f(100f, 100f), Vector2f(256f, 256f)))
        obj1.addComponent<SpriteRenderer>(SpriteRenderer(sprite = spriteSheet.sprites[0]))
        this.addGameObject(obj1)
    }

    private var spriteIndex = 0
    private val spriteFlipTime = 0.2f
    private var spriteFlipTimeLeft = 0.0f
    private var sprites: List<Sprite> = listOf()
    override fun update(dt: Float) {
        if (KeyListener.pressedKey(GLFW.GLFW_KEY_W)) camera.position.y -= dt * 50.0f
        if (KeyListener.pressedKey(GLFW.GLFW_KEY_S)) camera.position.y += dt * 50.0f
        if (KeyListener.pressedKey(GLFW.GLFW_KEY_A)) camera.position.x += dt * 50.0f
        if (KeyListener.pressedKey(GLFW.GLFW_KEY_D)) camera.position.x -= dt * 50.0f

        val obj1 = gameObjects[0]

        spriteFlipTimeLeft -= dt
        if (spriteFlipTimeLeft <= 0) {
            spriteFlipTimeLeft = spriteFlipTime
            spriteIndex++
            if (spriteIndex > 4) {
                spriteIndex = 0
            }
            obj1.getComponent(SpriteRenderer::class.java)?.sprite = sprites[spriteIndex]
        }

        for (gameObject in this.gameObjects) {
            gameObject.update(dt)
        }

        this.renderer.render()
    }
}