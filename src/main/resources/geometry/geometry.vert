#version 150 core

in vec3 pos;
in vec3 color;
in float sides;

out vec3 vColor;
out float vSides;

void main() {
    gl_Position = vec4(pos, 1.0);
    vColor = color;
    vSides = sides;
}
