package component

import org.joml.Vector2f
import renderer.DEFAULT_TEX_COORDS
import renderer.Texture

class Sprite(
    var texture: Texture? = null,
    var texCoords: Array<Vector2f> = DEFAULT_TEX_COORDS,
) {
    var width: Int = 0
    var height: Int = 0
}