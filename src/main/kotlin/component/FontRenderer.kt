package component

import core.Component

class FontRenderer : Component() {
    override fun start() {
        println("Starting Font Renderer")
        if(gameObject?.getComponent(SpriteRenderer::class.java) != null) {
            println("Found Sprite Renderer")
        }
    }

    override fun update(dt: Float) {
    }
}