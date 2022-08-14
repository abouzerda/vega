import core.VegaApplication
import core.Scene
import scene.MainScene

object Application : VegaApplication() {
    var scene: Scene = MainScene()

    init {
        showScene(scene)
    }
}