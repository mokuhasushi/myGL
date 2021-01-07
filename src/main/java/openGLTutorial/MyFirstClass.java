package openGLTutorial;

import myGL.Camera;
import myGL.FloatMatrix;

import myGL.Square;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;

public class MyFirstClass {

    private long window;

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();

        int vao = glGenVertexArrays();
        glBindVertexArray(vao);

        setUpTriangle();

        int program = makeProgram("simple.vert", "simple.frag");
        if (program < 0)
        {
            System.err.println("ERROR IN SHADERS");
            return;
        }
        glUseProgram(program);

        int checkerWidth = 128;
        int checkerHeight = 128;
        FloatBuffer pixels = makeCheckers(checkerWidth, checkerHeight);

        glActiveTexture(GL_TEXTURE0);
        makeTexture(pixels, checkerWidth, checkerHeight);
        memFree(pixels);

        pixels = makeBends(checkerWidth, checkerHeight, 16);

        glActiveTexture(GL_TEXTURE1);
        makeTexture(pixels, checkerWidth, checkerHeight);
        memFree(pixels);

        loop(program);

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private int makeTexture(FloatBuffer pixels, int width, int height) {
        int tex = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, tex);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_FLOAT, pixels);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glGenerateMipmap(GL_TEXTURE_2D);
        return tex;
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        window = glfwCreateWindow(800, 600, "Hello World!", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true);
        });

        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);

        glfwShowWindow(window);
        GL.createCapabilities();
    }

    private void loop(int program) {

        double start = 0;

        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        int posAttrib = glGetAttribLocation(program, "position");
        glEnableVertexAttribArray(posAttrib);
        glVertexAttribPointer(posAttrib, 3, GL_FLOAT, false, 4*8, 0);

        int colAttrib = glGetAttribLocation(program, "color");
        glEnableVertexAttribArray(colAttrib);
        glVertexAttribPointer(colAttrib, 3, GL_FLOAT, false, 4*8, 3*4);

        int texAttrib = glGetAttribLocation(program, "texcoord");
        glEnableVertexAttribArray(texAttrib);
        glVertexAttribPointer(texAttrib, 2, GL_FLOAT, false, 8*4, 6*4);

        glUniform1i(glGetUniformLocation(program, "texCheckers"), 0);
        glUniform1i(glGetUniformLocation(program, "texBends"), 1);

        Camera viewFrustum = new Camera((float)Math.toRadians(45.0), 4.0f/3.0f, 1.0f, 5.0f);
        int uniViewFrustum = glGetUniformLocation(program, "viewFrustum");
        FloatBuffer vf = memAllocFloat(4*4);
        vf.put(viewFrustum.getBuffer());
        vf.flip();
        glUniformMatrix4fv(uniViewFrustum, false, vf);
        memFree(vf);

//        int uniTex = glGetUniformLocation(program, "tex");
//        glUniform2f(uniTex, 0.0f,0.0f);
/*
        int uniColor = glGetUniformLocation(program, "triangleColor");
        glUniform3f(uniColor, 1.0f, 0.0f, 0.0f);
*/
        int uniTrans = glGetUniformLocation(program, "trans");
        int uniTras = glGetUniformLocation(program, "tras");
        int uniColor = glGetUniformLocation(program, "overrideColor");
        glUniform3f(uniColor, 1.0f, 1.0f, 1.0f);

        glEnable(GL_DEPTH_TEST);

        while ( !glfwWindowShouldClose(window) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            FloatMatrix trans = FloatMatrix.getRotationY((float)glfwGetTime()*12%360.0f);
            FloatBuffer fb = memAllocFloat(4*4);
            fb.put(trans.getBuffer());
            fb.flip();
            glUniformMatrix4fv(uniTrans, false, fb);
            memFree(fb);
            glUniform3f(uniTras, 0.0f, 0.0f, 2.5f);

//            glUniform3f(uniColor, ((float)Math.sin(glfwGetTime() - start * 4.0f) + 1.0f)/2.0f, 0.0f, 0.0f);
            glUniform1f(glGetUniformLocation(program, "time"), (float)Math.sin(glfwGetTime()) + 1.0f);


/*
            // Object Outlining
            glClearStencil(0);
            glClear(GL_STENCIL_BUFFER_BIT);

            glEnable(GL_STENCIL_TEST);

            glStencilFunc(GL_ALWAYS, 1, -1);
            glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);

            glDrawArrays(GL_TRIANGLES, 0, 36);

            glStencilFunc(GL_NOTEQUAL, 1, -1);
            glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);

            glLineWidth(3);
            glPolygonMode(GL_FRONT, GL_LINE);

            glDrawArrays(GL_TRIANGLES, 0, 36);
            glDisable(GL_STENCIL_TEST);
*/
            glDrawArrays(GL_TRIANGLES, 0, 36);

            glEnable(GL_STENCIL_TEST);

            glStencilFunc(GL_ALWAYS, 1, 0xff);
            glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
            glStencilMask(0xFF);
            glDepthMask(false);
            glClear(GL_STENCIL_BUFFER_BIT);

            glDrawArrays(GL_TRIANGLES, 36, 6);

            glStencilFunc(GL_EQUAL, 1, 0xFF);
            glStencilMask(0x00);
            glDepthMask(true);

            trans = FloatMatrix.getScaleMatrix(1, -1, 1).product(trans);
            fb = memAllocFloat(4*4);
            fb.put(trans.getBuffer());
            fb.flip();
            glUniformMatrix4fv(uniTrans, false, fb);
            memFree(fb);
            glUniform3f(uniTras, 0.0f, -1.0f, 2.5f);

            glUniform3f(uniColor, 0.3f, 0.3f, 0.6f);
            glDrawArrays(GL_TRIANGLES, 0, 36);
            glUniform3f(uniColor, 1.0f, 1.0f, 1.0f);

            glDisable(GL_STENCIL_TEST);


//            glDrawElements(GL_TRIANGLES, 3, GL_UNSIGNED_INT, 0);

            glfwSwapBuffers(window);

            glfwPollEvents();
        }
    }



    public void setUpTriangle() {
/*
        FloatBuffer vertices = memAllocFloat(3 * 3);
        vertices.put(new float[] {
                 0.0f,  0.5f, 0.2f,
                 0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, 0.8f,
        });
*/
/*
        FloatBuffer vertices = memAllocFloat(4 * 7);
        vertices.put(new float[] {
                -0.5f,  0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                 0.5f,  0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
                 0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
                -0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f
        });
*/
        FloatBuffer vertices = memAllocFloat(42*8);
        //-+ ++ +- --
        vertices.put(new Square(0.0f,0.0f,-0.5f,1.0f).getBufferArray());
        vertices.put(new Square(0.0f,0.0f,0.5f,1.0f).getBufferArray());
        vertices.put(new Square(-0.5f,0.0f,0.0f,1.0f, "X").getBufferArray());
        vertices.put(new Square(0.5f,0.0f,0.0f,1.0f, "X").getBufferArray());
        vertices.put(new Square(0.0f,-0.5f,0.0f,1.0f, "Y").getBufferArray());
        vertices.put(new Square(0.0f,0.5f,0.0f,1.0f, "Y").getBufferArray());
        vertices.put(new Square(0.0f,-0.5f,0.0f, 2.0f, new float[]{0.0f, 0.0f, 0.0f}, "Y").getBufferArray());
        vertices.flip();

        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        IntBuffer elements = memAllocInt(6);
        elements.put(new int[] {
                0, 1, 2,
                2, 3, 0
        });
        elements.flip();

        int ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elements, GL_STATIC_DRAW);

        memFree(vertices);
        memFree(elements);
    }
    public int makeProgram(String vertexShader, String fragShader) {

        int vertex_shader = make_shader(GL_VERTEX_SHADER, vertexShader);

        if (glGetShaderi(vertex_shader, GL_COMPILE_STATUS) == GL_FALSE){
            System.err.println(glGetShaderInfoLog(vertex_shader));
            return -1;
        }
        int fragment_shader = make_shader(GL_FRAGMENT_SHADER, fragShader);

        if (glGetShaderi(fragment_shader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println(glGetShaderInfoLog(fragment_shader));
            return -1;
        }
        int shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertex_shader);
        glAttachShader(shaderProgram, fragment_shader);

        glBindFragDataLocation(shaderProgram, 0, "outColor");

        glLinkProgram(shaderProgram);

        return shaderProgram;
    }
    public int make_shader(int type, String fileName) {
        int vertex_shader = glCreateShader(type);
        glShaderSource(vertex_shader, readFile("src/main/resources/" + fileName));
        glCompileShader(vertex_shader);

        return vertex_shader;
    }

    public FloatBuffer makeCheckers(int checkerWidth, int checkerHeight) {
        FloatBuffer pixels = memAllocFloat(3 * checkerWidth * checkerHeight);
        float[] zero = {0.0f, 0.0f, 0.0f};
        float[] uno = {1.0f, 1.0f, 1.0f};
        for (int i = 0; i < checkerWidth; i++) {
            for (int j = 0; j < checkerHeight; j++) {
                if ((i < checkerWidth/2 && j < checkerHeight/2) || (i >= checkerWidth/2 && j >= checkerHeight/2))
                    pixels.put(zero);
                else
                    pixels.put(uno);
            }
        }
        pixels.flip();
        return pixels;
    }
    public FloatBuffer makeBends(int checkerWidth, int checkerHeight, int bendHeight) {
        FloatBuffer pixels = memAllocFloat(3 * checkerWidth * checkerHeight);
        float[] zero = {0.0f, 0.0f, 0.0f};
        float[] uno = {1.0f, 1.0f, 1.0f};
        for (int i = 0; i < checkerWidth; i++) {
            for (int j = 0; j < checkerHeight; j++) {
                if (i / bendHeight % 2 == 0)
                    pixels.put(zero);
                else
                    pixels.put(uno);
            }
        }
        pixels.flip();
        return pixels;
    }

    public static void main(String[] args) {
        new MyFirstClass().run();
    }

    public static String readFile(String fileName) {
        try {
            return Files.readString(Path.of(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return Path.of(fileName).toString();
        }
    }

}
