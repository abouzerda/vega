package core

import org.joml.Vector2f

class GameObject(val name: String, var transform: Transform = Transform(), var zIndex: Int = 0) {
    private val components = mutableListOf<Component>()
    fun <T : Component> getComponent(componentClass: Class<T>): T? {
        val component = components.find { c -> componentClass.isAssignableFrom(c.javaClass) }
        return componentClass.cast(component)
    }

    fun <T : Component> addComponent(component: Component) {
        components.add(component)
        component.gameObject = this
    }

    fun <T : Component> removeComponent(componentClass: Class<T>) {
        components.removeIf { c -> componentClass.isAssignableFrom(c.javaClass) }
    }

    fun start() = components.forEach { it.start() }

    fun update(dt: Float) = components.forEach { it.update(dt) }
}