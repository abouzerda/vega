package renderer

import org.joml.*
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.GL_FALSE
import org.lwjgl.opengl.GL20.*
import utils.Utils


class Shader(private val filepath: String = "/default.glsl") {
    var programId = 0
    var vertexShaderId = 0
    var fragmentShaderId = 0
    var vertexShaderCode: String = ""
    var fragmentShaderCode: String = ""
    var active: Boolean = false

    init {
        programId = glCreateProgram();
        if (programId == 0) {
            throw Exception("Could not create Shader")
        }

    }

    fun parse() {
        val source = Utils.loadResource(filepath)
        val shaderSources = source.split("(#type)( )+([a-zA-Z]+)".toRegex())
        var index = source.indexOf("#type") + "#type ".length
        var eol = source.indexOf("\n", index)
        val firstDirective = source.substring(index, eol).trim()
        index = source.indexOf("#type", eol) + "#type ".length
        eol = source.indexOf("\n", index)
        val secondDirective = source.substring(index, eol).trim()
        if (firstDirective == "vertex" && secondDirective == "fragment") {
            vertexShaderCode = shaderSources[1]
            fragmentShaderCode = shaderSources[2]
        } else if (firstDirective == "fragment" && secondDirective == "vertex") {
            vertexShaderCode = shaderSources[2]
            fragmentShaderCode = shaderSources[1]
        }
    }

    fun compile() {
        vertexShaderId = compile(vertexShaderCode, GL_VERTEX_SHADER)
        fragmentShaderId = compile(fragmentShaderCode, GL_FRAGMENT_SHADER)
    }

    private fun compile(shaderCode: String, shaderType: Int): Int {
        val shaderId: Int = glCreateShader(shaderType)
        if (shaderId == 0) {
            throw Exception("Error creating shader. Type: $shaderType")
        }
        glShaderSource(shaderId, shaderCode)
        glCompileShader(shaderId)
        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
            throw Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024))
        }
        glAttachShader(programId, shaderId)
        return shaderId
    }

    fun link() {
        glLinkProgram(programId)
        if (glGetProgrami(programId, GL_LINK_STATUS) == GL_FALSE) {
            throw java.lang.Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024))
        }
        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId)
        }
        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId)
        }
        glValidateProgram(programId)
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == GL_FALSE) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024))
        }
    }

    fun bind() {
        if (!active) {
            glUseProgram(programId)
            active = true
        }
    }

    fun unbind() {
        glUseProgram(0)
        active = false
    }

    fun cleanup() {
        unbind()
        if (programId != 0) {
            glDeleteProgram(programId)
        }
    }

    fun uploadMat4f(varName: String, mat4: Matrix4f) {
        val varLocation = glGetUniformLocation(programId, varName)
        bind()
        val matBuffer = BufferUtils.createFloatBuffer(16)
        mat4[matBuffer]
        glUniformMatrix4fv(varLocation, false, matBuffer)
    }

    fun uploadMat3f(varName: String, mat3: Matrix3f) {
        val varLocation = glGetUniformLocation(programId, varName)
        bind()
        val matBuffer = BufferUtils.createFloatBuffer(9)
        mat3[matBuffer]
        glUniformMatrix3fv(varLocation, false, matBuffer)
    }

    fun uploadVec4f(varName: String, vec4: Vector4f) {
        val varLocation = glGetUniformLocation(programId, varName)
        bind()
        glUniform4f(varLocation, vec4.x, vec4.y, vec4.z, vec4.w)
    }

    fun uploadVec3f(varName: String, vec3: Vector3f) {
        val varLocation = glGetUniformLocation(programId, varName)
        bind()
        glUniform3f(varLocation, vec3.x, vec3.y, vec3.z)
    }

    fun uploadVec2f(varName: String, vec2: Vector2f) {
        val varLocation = glGetUniformLocation(programId, varName)
        bind()
        glUniform2f(varLocation, vec2.x, vec2.y)
    }

    fun uploadFloat(varName: String, value: Float) {
        val varLocation = glGetUniformLocation(programId, varName)
        bind()
        glUniform1f(varLocation, value)
    }

    fun uploadInt(varName: String, value: Int) {
        val varLocation = glGetUniformLocation(programId, varName)
        bind()
        glUniform1i(varLocation, value)
    }

    fun uploadTexture(varName: String, slot: Int) {
        val varLocation = glGetUniformLocation(programId, varName)
        bind()
        glUniform1i(varLocation, slot)
    }

    fun uploadIntArray(varName: String, array: IntArray) {
        val varLocation: Int = glGetUniformLocation(programId, varName)
        bind()
        glUniform1iv(varLocation, array)
    }
}