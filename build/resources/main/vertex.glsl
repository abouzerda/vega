#version 330 core

layout (location=0) in vec3 aPosition;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aTexCoords;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec2 fTexCoords;

void main()
{
    fColor = aColor;
    fTexCoords = aTexCoords;
    gl_Position = uProjection * uView * vec4(aPosition, 1.0);
}