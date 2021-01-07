#version 150 core

layout(points) in;
layout(line_strip, max_vertices = 18) out; //*9

in vec3 vColor[];
out vec3 fColor;

in float vEdge[];

in mat4 vRotation[];

uniform mat4 model;

mat4 rotate90Right = mat4(
    vec4(0,1,0,0),
    vec4(1,0,0,0),
    vec4(0,0,0,0),
    vec4(0,0,0,0)
);
mat4 id = mat4(
    vec4(1,0,0,0),
    vec4(0,1,0,0),
    vec4(0,0,1,0),
    vec4(0,0,0,1)
);

vec4 peanoBase(vec4 start, float edge, mat4 rotation) {
    vec4 cur = start;
    vec4 N = rotation * vec4(0, 1, 0, 0);
    vec4 S = rotation * vec4(0, -1, 0, 0);
    vec4 E = rotation * vec4(1, 0, 0, 0);
    vec4 W = rotation * vec4(-1, 0, 0, 0);

    float longEdge = 5.0/18 * edge;
    float middleEdge = 4.0/18 * edge;
    float shortEdge = 1.0/18 * edge;
    { gl_Position = start * model;
        EmitVertex();

        cur += E * longEdge;
        gl_Position = cur * model;
        EmitVertex();

        cur += E * shortEdge + N * shortEdge;
        gl_Position = cur * model;
        EmitVertex();

        cur += N * middleEdge;
        gl_Position = cur * model;
        EmitVertex();

        cur += N * shortEdge + E * shortEdge;
        gl_Position = cur * model;
        EmitVertex();

        cur += E * middleEdge;
        gl_Position = cur * model;
        EmitVertex();

        cur += E * shortEdge + S * shortEdge;
        gl_Position = cur * model;
        EmitVertex();

        cur += S * middleEdge;
        gl_Position = cur * model;
        EmitVertex();

        cur += S * shortEdge + W * shortEdge;
        gl_Position = cur * model;
        EmitVertex();

        cur += W * middleEdge;
        gl_Position = cur * model;
        EmitVertex();

        cur += W * shortEdge + S * shortEdge;
        gl_Position = cur * model;
        EmitVertex();

        cur += S * middleEdge;
        gl_Position = cur * model;
        EmitVertex();

        cur += E * shortEdge + S * shortEdge;
        gl_Position = cur * model;
        EmitVertex();

        cur += E * middleEdge;
        gl_Position = cur * model;
        EmitVertex();

        cur += E * shortEdge + N * shortEdge;
        gl_Position = cur * model;
        EmitVertex();

        cur += N * middleEdge;
        gl_Position = cur * model;
        EmitVertex();

        cur += N * shortEdge + E * shortEdge;
        gl_Position = cur * model;
        EmitVertex();

        cur += E * longEdge;
        gl_Position = cur * model;
        EmitVertex();
    }
    return cur;

}

vec4 peano(vec4 start, float edge, mat4 rotation, int level) {
    vec4 cur = start;

    float normalEdge = edge / 3.0;


    cur  = peanoBase(cur, normalEdge, id * rotation);
    cur  = peanoBase(cur, normalEdge, -rotate90Right * rotation);
    cur  = peanoBase(cur, normalEdge, id * rotation);
    cur  = peanoBase(cur, normalEdge, rotate90Right * rotation);
    cur  = peanoBase(cur, normalEdge, -id * rotation);
    cur  = peanoBase(cur, normalEdge, rotate90Right * rotation);
    cur  = peanoBase(cur, normalEdge, id * rotation);
    cur  = peanoBase(cur, normalEdge, -rotate90Right * rotation);
    cur  = peanoBase(cur, normalEdge, id * rotation);

    return cur;
}

void main() {
    fColor = vColor[0];

    vec4 cur;

    int level = 1;

    cur = peanoBase(gl_in[0].gl_Position, vEdge[0], vRotation[0]);
/*
    cur = peano(cur, edge, rotate90Right, level);
    cur = peano(cur, edge, -id, level);
    cur = peano(cur, edge, -rotate90Right, level);
*/

    EndPrimitive();
}
