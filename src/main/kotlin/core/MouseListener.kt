package core

class MouseListener {
    companion object {
        var cursorPosX: Double = 0.0
        var cursorPosY: Double = 0.0

        fun cursorPosCallback(window: GLFWWindow, x: Double, y: Double) {
            cursorPosX = x
            cursorPosY = y
        }
    }
}