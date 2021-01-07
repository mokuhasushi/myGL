#version 150 core

in vec2 pos;
in vec3 color;

out vec3 vColor;

uniform mat4 model;

void main() {
    gl_Position = model * vec4(pos, 0.0, 1.0);
    vColor = color;
}
