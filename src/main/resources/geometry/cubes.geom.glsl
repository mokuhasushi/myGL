#version 150 core

layout(points) in;
layout(line_strip, max_vertices = 16) out;

in vec3 vColor[];
out vec3 fColor;

uniform mat4 view;
uniform mat4 proj;
uniform mat4 model;
//uniform vec3 tras;

void main() {
    fColor = vColor[0];

    gl_Position = proj * view * model * (/*vec4(tras, 0.0) + */gl_in[0].gl_Position);

    float size = 0.05;

    vec4 NEU = proj * view * model * vec4( size,  size,  size, 0.0);
    vec4 NED = proj * view * model * vec4( size, -size,  size, 0.0);
    vec4 NWU = proj * view * model * vec4( size,  size, -size, 0.0);
    vec4 NWD = proj * view * model * vec4( size, -size, -size, 0.0);
    vec4 SEU = proj * view * model * vec4(-size,  size,  size, 0.0);
    vec4 SED = proj * view * model * vec4(-size, -size,  size, 0.0);
    vec4 SWU = proj * view * model * vec4(-size,  size, -size, 0.0);
    vec4 SWD = proj * view * model * vec4(-size, -size, -size, 0.0);

    // Create a cube centered on the given point.
    gl_Position = gl_in[0].gl_Position + NED;
    EmitVertex();

    gl_Position = gl_in[0].gl_Position + NWD;
    EmitVertex();

    gl_Position = gl_in[0].gl_Position + SWD;
    EmitVertex();

    gl_Position = gl_in[0].gl_Position + SED;
    EmitVertex();

    gl_Position = gl_in[0].gl_Position + SEU;
    EmitVertex();

    gl_Position = gl_in[0].gl_Position + SWU;
    EmitVertex();

    gl_Position = gl_in[0].gl_Position + NWU;
    EmitVertex();

    gl_Position = gl_in[0].gl_Position + NEU;
    EmitVertex();

    gl_Position = gl_in[0].gl_Position + NED;
    EmitVertex();

    gl_Position = gl_in[0].gl_Position + SED;
    EmitVertex();

    gl_Position = gl_in[0].gl_Position + SEU;
    EmitVertex();

    gl_Position = gl_in[0].gl_Position + NEU;
    EmitVertex();

    gl_Position = gl_in[0].gl_Position + NWU;
    EmitVertex();

    gl_Position = gl_in[0].gl_Position + NWD;
    EmitVertex();

    gl_Position = gl_in[0].gl_Position + SWD;
    EmitVertex();

    gl_Position = gl_in[0].gl_Position + SWU;
    EmitVertex();

    EndPrimitive();
/*
    gl_Position = gl_in[0].gl_Position + vec4(size, size, -size, 0.0);
    EmitVertex();
    gl_Position = gl_in[0].gl_Position + vec4(-size, size, -size, 0.0);
    EmitVertex();
    gl_Position = gl_in[0].gl_Position + vec4(-size, -size, -size, 0.0);
    EmitVertex();
    gl_Position = gl_in[0].gl_Position + vec4(size, -size, -size, 0.0);
    EmitVertex();

    gl_Position = gl_in[0].gl_Position + vec4(size, size, size, 0.0);
    EmitVertex();
    gl_Position = gl_in[0].gl_Position + vec4(size, -size, size, 0.0);
    EmitVertex();
    gl_Position = gl_in[0].gl_Position + vec4(-size, -size, size, 0.0);
    EmitVertex();
    gl_Position = gl_in[0].gl_Position + vec4(-size, size, size, 0.0);
    EmitVertex();

    gl_Position = gl_in[0].gl_Position + vec4(size, size, size, 0.0);
    EmitVertex();
    gl_Position = gl_in[0].gl_Position + vec4(size, size, -size, 0.0);
    EmitVertex();
    gl_Position = gl_in[0].gl_Position + vec4(size, -size, -size, 0.0);
    EmitVertex();
    gl_Position = gl_in[0].gl_Position + vec4(size, -size, size, 0.0);
    EmitVertex();

    gl_Position = gl_in[0].gl_Position + vec4(-size, size, size, 0.0);
    EmitVertex();
    gl_Position = gl_in[0].gl_Position + vec4(-size, -size, size, 0.0);
    EmitVertex();
    gl_Position = gl_in[0].gl_Position + vec4(-size, -size, -size, 0.0);
    EmitVertex();
    gl_Position = gl_in[0].gl_Position + vec4(-size, size, -size, 0.0);
    EmitVertex();

    gl_Position = gl_in[0].gl_Position + vec4(size, size, size, 0.0);
    EmitVertex();
    gl_Position = gl_in[0].gl_Position + vec4(-size, size, size, 0.0);
    EmitVertex();
    gl_Position = gl_in[0].gl_Position + vec4(-size, size, -size, 0.0);
    EmitVertex();
    gl_Position = gl_in[0].gl_Position + vec4(size, size, -size, 0.0);
    EmitVertex();

    gl_Position = gl_in[0].gl_Position + vec4(size, -size, size, 0.0);
    EmitVertex();
    gl_Position = gl_in[0].gl_Position + vec4(size, -size, -size, 0.0);
    EmitVertex();
    gl_Position = gl_in[0].gl_Position + vec4(-size, -size, -size, 0.0);
    EmitVertex();
    gl_Position = gl_in[0].gl_Position + vec4(-size, -size, size, 0.0);
    EmitVertex();
*/

    EndPrimitive();
}
