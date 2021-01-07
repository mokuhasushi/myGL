#version 150 core

in vec2 position;
in vec3 color;
in vec2 texcoord;

out vec3 Color;
out vec2 Texcoord;

uniform mat4 trans;
uniform mat4 viewFrustum;
uniform vec3 tras;
uniform vec3 overrideColor;

void main() {
    Color = overrideColor * color;
    Texcoord = texcoord;
    gl_Position = viewFrustum *(vec4(tras, 0.0) + trans * vec4(position, 1.0, 1.0));
}
