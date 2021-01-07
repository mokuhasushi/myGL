//http://duriansoftware.com/joe/An-intro-to-modern-OpenGL.-Chapter-2.1:-Buffers-and-Textures.html
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.*;

public class IntroToModernOpenGL {

    public static void main (String[] args){
        glfwInit();
        long window = createWindow();

        FloatBuffer buffer = memAllocFloat(4*2);

        buffer.put(new float[]{-1.0f, -1.0f,
                                0.5f, -1.0f,
                                -0.5f, 1.0f,
                                1.0f, 1.0f});

        buffer.flip();

        ShortBuffer element_buffer = memAllocShort(4);
        element_buffer.put(new short[]{0,1,2,3});
        element_buffer.flip();

        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

        memFree(buffer);

        int e_vbo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, e_vbo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, element_buffer, GL_STATIC_DRAW);

        memFree(element_buffer);

        ByteBuffer tex_buffer = BufferUtils.createByteBuffer(32*32*3);
        for (int i = 0; i < 32; i++) {
            for (int j = 0; j < 32; j++) {
                tex_buffer.put(new byte[]{127, 0, 0});
//                tex_buffer.put(new byte[]{(byte) (i+j%3), (byte) (i+j%3 + 1), (byte) (i+j%3 + 2)});
            }
        }

        tex_buffer.flip();

        glEnable(GL_TEXTURE_2D);

        int texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB8, 32, 32, 0, GL_RGB, GL_UNSIGNED_BYTE, tex_buffer);

//        memFree(tex_buffer);

        int vert_shader_id = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vert_shader_id,
                "#version 110\n" +
                "\n" +
                "attribute vec2 position;\n" +
                "\n" +
                "varying vec2 texcoord;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "    gl_Position = vec4(position, 0.0, 1.0);\n" +
                "    texcoord = position * vec2(0.5) + vec2(0.5);\n" +
                "}");
        glCompileShader(vert_shader_id);

        int frag_shader_id = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(frag_shader_id,
                "#version 110\n" +
                        "\n" +
//                        "uniform float fade_factor;\n" +
                        "uniform sampler2D textures[1];\n" +
                        "\n" +
                        "varying vec2 texcoord;\n" +
                        "\n" +
                        "void main()\n" +
                        "{\n" +
//                        "    gl_FragColor = " + //mix(\n" +
                        "    gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);\n" +
/*
                        "        texture2D(textures[0], texcoord" + //),\n" +
//                        "        texture2D(textures[1], texcoord),\n" +
//                        "        fade_factor\n" +
                        "    );\n" +
*/
                        "}"
        );
/*
        glShaderSource(frag_shader_id, "void main() {\n" +
                                             "    gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);\n" +
                                             "}\n");
*/
        glCompileShader(frag_shader_id);

        int program = glCreateProgram();
        glAttachShader(program, vert_shader_id);
        glAttachShader(program, frag_shader_id);
        glLinkProgram(program);

        programLogToErr(program);

        glUseProgram(program);

        glEnableClientState(GL_VERTEX_ARRAY);
        glVertexPointer(2, GL_FLOAT, 0, 0L);
        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();

            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texID);

            glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

            glfwSwapBuffers(window);
        }
        glfwTerminate();

    }

    private static void programLogToErr(int program){
        int comp = glGetProgrami(program, GL_LINK_STATUS);
        int len = glGetProgrami(program, GL_INFO_LOG_LENGTH);
        String err = glGetProgramInfoLog(program, len);

        String log = "";
        if (err != null && err.length() != 0)
            log = err + "\n" + log;
        if (log != null)
            log = log.trim();
        if (comp == GL_FALSE)
            System.err.println(log.length()!=0 ? log : "Could not link program");


    }

    private static long createWindow(){
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        long window = glfwCreateWindow(800, 600, "IntoModernGL", NULL, NULL);

        glfwMakeContextCurrent(window);
        createCapabilities();

        return window;
    }
}
