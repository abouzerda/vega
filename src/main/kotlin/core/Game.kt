package core

import scene.MainScene


class Game : Application() {
    var activeScene: Scene = MainScene()

    override fun init() {
        activeScene.init()
    }

    override fun update(dt: Double) {
        activeScene.update(dt)
    }
}