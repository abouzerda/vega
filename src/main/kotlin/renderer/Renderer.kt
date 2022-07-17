package renderer

import component.SpriteRenderer
import core.GameObject
import java.util.*

class Renderer {
    private val batches: MutableList<Batch>
    fun add(go: GameObject) {
        val spr: SpriteRenderer? = go.getComponent(SpriteRenderer::class.java)
        if (spr != null) {
            add(spr)
        }
    }

    private fun add(sprite: SpriteRenderer) {
        var added = false
        for (batch in batches) {
            if (batch.hasRoom()) {
                batch.addSprite(sprite)
                added = true
                break
            }
        }
        if (!added) {
            val newBatch = Batch(MAX_BATCH_SIZE)
            newBatch.start()
            batches.add(newBatch)
            newBatch.addSprite(sprite)
        }
    }

    fun render() {
        for (batch in batches) {
            batch.render()
        }
    }

    init {
        batches = ArrayList<Batch>()
    }
}