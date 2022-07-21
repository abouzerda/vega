package core

import component.Component

class GameObject(val name: String, var transform: Transform = Transform(), var zIndex: Int = 0) {
    private val components = mutableListOf<Component>()
    fun <T : Component> getComponent(componentClass: Class<T>): T? {
        val component = components.find { c -> componentClass.isAssignableFrom(c.javaClass) }
        return componentClass.cast(component)
    }

    fun <T : Component> addComponent(component: Component) {
        component.gameObject = this
        components.add(component)
    }

    fun <T : Component> removeComponent(componentClass: Class<T>) {
        components.removeIf { c -> componentClass.isAssignableFrom(c.javaClass) }
    }

    fun start() = components.forEach {
        it.gameObject = this
        it.start()
    }

    fun update(dt: Float) = components.forEach { it.update(dt) }

    fun imgui() = components.forEach { it.imgui() }
}