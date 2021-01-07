#version 150 core

in vec3 Color;
in vec2 Texcoord;

out vec4 outColor;

uniform sampler2D texCheckers;
uniform sampler2D texBends;

uniform float time;

void main() {
    vec4 colCheckers = texture(texCheckers, Texcoord.y - 0.5f > 0? vec2((Texcoord.x + sin(Texcoord.y * 60.0 + time * 2.0) / 30.0), 1.0-Texcoord.y) : Texcoord);
    vec4 colBends = texture(texBends, Texcoord.y - 0.5f > 0? vec2((Texcoord.x + sin(Texcoord.y * 60.0 + time * 2.0) / 30.0), 1.0-Texcoord.y) : Texcoord);
    vec4 texColor = mix(colBends, colCheckers, time);
    outColor = vec4(Color, 1.0) * texColor;
}
