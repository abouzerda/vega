package core

abstract class Component {
    var gameObject: GameObject? = null

    abstract fun start()
    abstract fun update(dt : Float)
}
