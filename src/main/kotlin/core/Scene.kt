package core

import imgui.ImGui
import io.KeyEvent
import io.MouseEvent
import org.joml.Vector2f
import renderer.Renderer
import utils.Utils
import java.io.File
import java.util.*

abstract class Scene {
    protected var renderer: Renderer = Renderer()
    var camera: Camera = Camera(Vector2f())
    protected var gameObjects = mutableListOf<GameObject>()
    protected var active = false
    protected var activeGameObject: Optional<GameObject> = Optional.empty()

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

    private fun addGameObject(gameObject: GameObject) {
        gameObjects.add(gameObject)
        if (active) {
            gameObject.start()
            this.renderer.add(gameObject)
        }
    }

    abstract fun init()
    abstract fun update(dt: Float)
    fun sceneImgui() {
        activeGameObject.ifPresent {
            ImGui.begin("Inspector")
            it.imgui()
            ImGui.end()
        }
        imgui()
    }

    open fun imgui() {}

    internal fun load() {
        with(File(SAVE_FILE_NAME)) {
            val json = readText()
            for (gameObject in Utils.gson.fromJson(json, Array<GameObject>::class.java)) {
                addGameObject(gameObject)
            }

        }
    }

    internal fun save() {
        with(File(SAVE_FILE_NAME)) {
            val json = Utils.gson.toJson(gameObjects)
            writeText(json)
        }
    }
}