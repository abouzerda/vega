package core

import component.Sprite
import component.SpriteRenderer
import org.joml.Vector2f

object Prefabs {
    fun generateSpriteObject(sprite: Sprite, sizeX: Float, sizeY: Float): GameObject = GameObject(
        "Sprite_Object_Gen", Transform(Vector2f(), Vector2f(sizeX, sizeY)), 0
    ).apply { addComponent<SpriteRenderer>(SpriteRenderer(sprite = sprite)) }
}