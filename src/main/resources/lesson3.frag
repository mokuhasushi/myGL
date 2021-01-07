#version 120

uniform sampler2D texture0;
uniform sampler2D texture1;

varying float fade_factor;
varying vec2 texcoord;

void main()
{
//    gl_FragColor = vec4(texcoord * fade_factor, fade_factor*0.5, 1.0);

    gl_FragColor = vec4(texcoord * fade_factor, fade_factor*0.5, 1.0);
/*

    gl_FragColor = mix(
        texture2D(texture0, texcoord),
        texture2D(texture1, texcoord),
        fade_factor
    );
*/
}