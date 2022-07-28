package core

import component.Component
import io.KeyEvent
import io.MouseEvent
import org.joml.Vector2f
import renderer.Renderer
import utils.Utils
import java.io.File
import java.lang.Integer.max
import java.util.*

abstract class Scene {
    protected var renderer: Renderer = Renderer()
    var camera: Camera = Camera(Vector2f())
    var gameObjects = mutableListOf<GameObject>()
    private var active = false
    var activeGameObject: Optional<GameObject> = Optional.empty()

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

    internal fun addGameObject(gameObject: GameObject) {
        gameObjects.add(gameObject)
        if (active) {
            gameObject.start()
            this.renderer.add(gameObject)
        }
    }

    abstract fun init()
    abstract fun update(dt: Float)
    open fun imgui() {}

    internal fun load() {
        val file = File(SAVE_FILE_NAME)
        if (file.createNewFile()) {
            file.writeText(Utils.gson.toJson(emptyList<GameObject>()))
        }
        with(file) {
            var maxGameObjectId = 0
            var maxComponentId = 0
            val json = readText()
            for (gameObject in Utils.gson.fromJson(json, Array<GameObject>::class.java)) {
                addGameObject(gameObject)
                maxGameObjectId = max(gameObject.id, maxGameObjectId)
                maxComponentId = max(gameObject.components.maxOfOrNull { it.id } ?: 0, maxComponentId)
            }
            GameObject.ID_COUNTER = maxGameObjectId
            Component.ID_COUNTER = maxComponentId
        }
    }

    internal fun save() {
        with(File(SAVE_FILE_NAME)) {
            val json = Utils.gson.toJson(gameObjects)
            writeText(json)
        }
    }

    abstract fun render()
}