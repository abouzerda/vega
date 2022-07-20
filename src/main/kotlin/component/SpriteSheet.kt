package component

import org.joml.Vector2f
import renderer.Texture

class SpriteSheet(
    texture: Texture,
    spriteWidth: Int,
    spriteHeight: Int,
    numSprites: Int,
    spacing: Int
) {
    val sprites: MutableList<Sprite> = mutableListOf()

    init {
        var currentX = 0f
        var currentY: Float = (texture.height - spriteHeight).toFloat()
        for (i in 0 until numSprites) {
            val topY : Float = (currentY + spriteHeight) / texture.height
            val rightX : Float = (currentX + spriteWidth) / texture.width
            val leftX : Float = currentX / texture.width
            val bottomY : Float = currentY / texture.height
            val texCoords = arrayOf(
                Vector2f(rightX, topY),
                Vector2f(rightX, bottomY),
                Vector2f(leftX, bottomY),
                Vector2f(leftX, topY)
            )
            val sprite = Sprite(texture, texCoords)
            sprites.add(sprite)
            currentX += spriteWidth + spacing
            if (currentX >= texture.width) {
                currentX = 0f
                currentY -= spriteHeight + spacing
            }
        }
    }
}