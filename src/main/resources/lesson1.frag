#version 110
uniform float fade_factor;
uniform sampler2D textures0;
varying vec2 texcoord;
void main()
{
    gl_FragColor = vec4(texcoord * fade_factor, fade_factor*0.5, 1.0);
}
