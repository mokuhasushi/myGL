#version 120

uniform sampler2D texture0;
uniform sampler2D texture1;

varying float fade_factor;
varying vec2 texcoord;

vec4 posToCol()
{
    vec4 first_color = vec4(1.0, 1.0, 1.0, 1.0);
    vec4 second_color = vec4(0.0, 0.0, 0.0, 1.0);
    vec4 third_color = vec4(0.5, 0.5, 0.5, 1.0);


    float sin60 = sqrt(3)/2;
    float edge = 0.1f;
    float height = edge * sin60;
    float width = edge * 0.5;

    int posX = int(floor(texcoord.x/ width));
    int posY = int(floor(texcoord.y/ height));

    int value = int(mod(posX + posY*3, 6));

    if (value == 0)
        return (mod(texcoord.y, height) < mod(texcoord.x, width) * sqrt(3) ? second_color : first_color);
    else if (value == 1)
        return second_color;
    else if (value == 2)
        return (mod(texcoord.y, height) < mod(texcoord.x, width) * sqrt(3) ? first_color : second_color);
    else if (value == 3)
        return (mod(texcoord.y, height) > height - mod(texcoord.x, width) * sqrt(3) ? third_color : first_color);
    else if (value == 4)
        return third_color;
    else if (value == 5)
        return (mod(texcoord.y, height) > height - mod(texcoord.x, width) * sqrt(3) ? first_color : third_color);

    /*
        switch (value)
        {
            case 0:
                if (mod(texcoord.y ,height) < (mod(texcoord.x , width)*sqrt(3)))
                    return second_color;
                else
                    return first_color;
            case 1:
                return second_color;
            case 2:
                return (mod(texcoord.y, height) < mod(texcoord.x, width) * sqrt(3) ? first_color : second_color);
            case 3:
                return (mod(texcoord.y, height) > height - mod(texcoord.x, width) * sqrt(3) ? third_color : first_color);
            case 4:
                return third_color;
            case 5:
                return (mod(texcoord.y, height) > height - mod(texcoord.x, width) * sqrt(3) ? first_color : third_color);
        }
    */


    return vec4(1.0,0.0,0.0,1.0);
}

void main()
{
    gl_FragColor = posToCol();
//    gl_FragColor = vec4(texcoord * fade_factor, fade_factor*0.5, 1.0);
//    gl_FragColor = floor(texcoord.x * 10)%2 == 0 ? vec4(1.0,0.0,0.0,1.0) : vec4(0.0,1.0,0.0,0.0);
//    gl_FragColor = posToCol();
    /*

        gl_FragColor = mix(
            texture2D(texture0, texcoord),
            texture2D(texture1, texcoord),
            fade_factor
        );
    */
}