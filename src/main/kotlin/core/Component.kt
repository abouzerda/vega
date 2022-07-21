package core


abstract class Component {
    @Transient
    lateinit var gameObject: GameObject

    abstract fun start()
    abstract fun update(dt : Float)
    open fun imgui() {}
}
