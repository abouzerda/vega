package core

import org.joml.Vector2f
import renderer.Renderer

abstract class Scene {
    protected var renderer: Renderer = Renderer()
    var camera: Camera = Camera(Vector2f())
    protected var gameObjects = mutableListOf<GameObject>()
    protected var active = false

    /* Key input event handlers */
    var onKeyPress: ((KeyEvent) -> Unit) = {}
    var onKeyRelease: ((KeyEvent) -> Unit) = {}

    /* Mouse input event handlers */
    var onMousePress: ((MouseEvent) -> Unit) = {}
    var onMouseRelease: ((MouseEvent) -> Unit) = {}
    var onMouseEnter: ((MouseEvent) -> Unit) = {}
    var onMouseExit: ((MouseEvent) -> Unit) = {}
    var onMouseMove: ((MouseEvent) -> Unit) = {}
    var onMouseScroll: ((MouseEvent) -> Unit) = {}

    fun start() {
        gameObjects.forEach {
            it.start()
            renderer.add(it)
        }
        active = true
    }
    fun addGameObject(gameObject: GameObject) {
        gameObjects.add(gameObject)
        if (active) {
            gameObject.start()
            this.renderer.add(gameObject)
        }
    }

    abstract fun init()
    abstract fun update(dt: Float)
}