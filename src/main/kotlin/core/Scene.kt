package core

abstract class Scene {
    protected lateinit var camera: Camera
    protected var gameObjects = mutableListOf<GameObject>()
    protected var active = false

    fun start() {
        gameObjects.forEach { it.start() }
        active = true
    }
    fun addGameObject(gameObject: GameObject) {
        gameObjects.add(gameObject)
        if (active) gameObject.start()
    }

    abstract fun init()
    abstract fun update(dt: Float)
}