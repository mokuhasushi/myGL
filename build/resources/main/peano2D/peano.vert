#version 150 core

in vec2 pos;
in vec3 color;
in float edge;
in vec4 rotation2x2;

out vec3 vColor;
out float vEdge;

out mat4 vRotation;

void main() {
    gl_Position = vec4(pos, 0.0, 1.0);
    vColor = color;
    vEdge = edge;
    vRotation = mat4(
        vec4(rotation2x2.x, rotation2x2.y, 0, 0),
        vec4(rotation2x2.z, rotation2x2.w, 0, 0),
        vec4(0, 0, 0, 0),
        vec4(0, 0, 0, 0)
    );
}
