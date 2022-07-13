package core

import scene.MainScene


class Game : Application() {
    var activeScene: Scene = MainScene()

    override fun init() {
        activeScene.init()
        activeScene.start()
    }

    override fun update(dt: Float) {
        activeScene.update(dt)
    }
}