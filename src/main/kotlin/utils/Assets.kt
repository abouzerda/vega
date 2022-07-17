package utils

import renderer.Shader
import renderer.Texture

object Assets {
    var shaders: MutableMap<String, Shader> = HashMap()
    var textures: MutableMap<String, Texture> = HashMap()

    fun getShader(filepath: String): Shader {
        if (shaders.containsKey(filepath)) return shaders[filepath]!!
        val shader = Shader(filepath).apply {
            parse()
            compile()
            link()
        }
        shaders[filepath] = shader
        return shader
    }

    fun getTexture(filepath: String): Texture {
        if (textures.containsKey(filepath)) return textures[filepath]!!
        val texture = Texture(filepath)
        textures[filepath] = texture
        return texture
    }
}