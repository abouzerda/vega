import core.LWJGLApplication
import core.Scene
import scene.MainScene

object Application : LWJGLApplication() {
    var scene: Scene = MainScene()

    init {
        showScene(scene)
    }
}