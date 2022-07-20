package utils

import component.SpriteSheet
import renderer.Shader
import renderer.Texture

object Assets {
    var shaders: MutableMap<String, Shader> = HashMap()
    var textures: MutableMap<String, Texture> = HashMap()
    var spriteSheets: MutableMap<String, SpriteSheet> = HashMap()

    fun loadShader(filepath: String): Shader {
        if (shaders.containsKey(filepath)) return shaders[filepath]!!
        val shader = Shader(filepath).apply {
            parse()
            compile()
            link()
        }
        shaders[filepath] = shader
        return shader
    }

    fun loadTexture(filepath: String): Texture {
        if (textures.containsKey(filepath)) return textures[filepath]!!
        val texture = Texture(filepath)
        textures[filepath] = texture
        return texture
    }

    fun loadSpriteSheet(
        filepath: String,
        spriteWidth: Int,
        spriteHeight: Int,
        numSprites: Int,
        spacing: Int
    ): SpriteSheet {
        if (spriteSheets.containsKey(filepath)) return spriteSheets[filepath]!!
        val spriteSheet = SpriteSheet(
            texture = loadTexture(filepath),
            spriteWidth = spriteWidth,
            spriteHeight = spriteHeight,
            numSprites = numSprites,
            spacing = spacing
        )
        spriteSheets[filepath] = spriteSheet
        return spriteSheet
    }
}