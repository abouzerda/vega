package renderer

import component.SpriteRenderer
import core.GameObject
import java.util.*

class Renderer {
    private val batches: MutableList<Batch>

    companion object {
        lateinit var currentShader: Shader
    }

    fun add(go: GameObject) {
        val spr: SpriteRenderer? = go.getComponent(SpriteRenderer::class.java)
        if (spr != null) {
            add(spr)
        }
    }

    private fun add(sprite: SpriteRenderer) {
        var added = false
        for (batch in batches) {
            if (batch.hasRoom() && batch.zIndex == sprite.gameObject.zIndex) {
                batch.addSprite(sprite)
                added = true
                break
            }
        }
        if (!added) {
            val newBatch = Batch(MAX_BATCH_SIZE, sprite.gameObject.zIndex)
            newBatch.start()
            batches.add(newBatch)
            newBatch.addSprite(sprite)
            batches.sort()
        }
    }

    fun render() {
        currentShader.bind()
        for (batch in batches) {
            batch.render()
        }
    }

    init {
        batches = ArrayList<Batch>()
    }
}