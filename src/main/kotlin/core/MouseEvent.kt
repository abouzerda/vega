package core

import org.lwjgl.glfw.GLFW.*
import java.util.*

class MouseEvent(
    val xPos: Double = 0.0,
    val yPos: Double = 0.0,
    val entered: Boolean = false,
    val button: Int = GLFW_KEY_UNKNOWN,
    val action: Optional<Int> = Optional.empty(),
    val mods: Int = 0,
    val xScrollOffset: Double = 0.0,
    val yScrollOffset: Double = 0.0
)