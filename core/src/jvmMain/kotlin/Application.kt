import core.VegaApplication
import core.Scene
import scene.MainScene
import scene.TestScene

object Application : VegaApplication() {
    var scene: Scene = TestScene()

    init {
        showScene(scene)
    }
}