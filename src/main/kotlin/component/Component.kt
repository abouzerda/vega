package component

import core.GameObject
import imgui.ImGui
import org.joml.Vector3f
import org.joml.Vector4f
import java.lang.reflect.Modifier


abstract class Component {
    var id = 0

    companion object {
        var ID_COUNTER: Int = 0
    }

    @Transient
    lateinit var gameObject: GameObject

    abstract fun start()
    abstract fun update(dt: Float)
    open fun imgui() {
        val fields = javaClass.declaredFields
        fields.forEach {
            if (Modifier.isTransient(it.modifiers)) return@forEach
            val isPrivate = Modifier.isPrivate(it.modifiers)
            if (isPrivate) it.isAccessible = true

            val type = it.type
            val value = it.get(this)
            val name = it.name

            when (type) {
                Int::class.java -> {
                    val values = intArrayOf(value as Int)
                    if (ImGui.dragInt(name, values)) it.set(this, values[0])
                }
                Float::class.java -> {
                    val values = floatArrayOf(value as Float)
                    if (ImGui.dragFloat(name, values)) it.set(this, values[0])
                }
                Boolean::class.java -> {
                    if (ImGui.checkbox(name, value as Boolean)) it.set(this, !value)
                }
                Vector3f::class.java -> {
                    with(value as Vector3f) {
                        val values = floatArrayOf(value.x, value.y, value.z)
                        if (ImGui.dragFloat3(name, values)) set(values)
                    }
                }
                Vector4f::class.java -> {
                    with(value as Vector4f) {
                        val values = floatArrayOf(value.x, value.y, value.z, value.w)
                        if (ImGui.dragFloat4(name, values)) set(values)
                    }
                }
            }

            if (isPrivate) it.isAccessible = false
        }
    }

    fun generateId() {
        if (id == 0) this.id = ++ID_COUNTER
    }
}
