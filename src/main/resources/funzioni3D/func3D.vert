#version 150 core

in vec3 pos;
in vec3 color;

out vec3 vColor;

uniform mat4 model;
uniform mat4 view;
uniform mat4 proj;

void main() {
    gl_Position = proj * view * model * vec4(pos, 1.0);
    vColor = color;
}
