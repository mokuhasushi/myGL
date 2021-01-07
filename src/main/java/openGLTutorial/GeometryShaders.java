package openGLTutorial;

import myGL.Camera;
import myGL.FloatMatrix;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;


public class GeometryShaders {
    private long window;

    public void run () {
        System.out.println("Geometry shaders " + Version.getVersion() + "!");

        init();

        int program = makeProgram("geometry.vert", "geometry.frag", "cubes.geom.glsl");
        if (program < 0)
        {
            System.err.println("ERROR IN SHADERS");
            return;
        }
        glUseProgram(program);

        makePoints();

        int vao = glGenVertexArrays();
        glBindVertexArray(vao);

        loop(program);

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();

    }

    private void loop(int program) {
/*
        Camera viewFrustum = new Camera((float)Math.toRadians(45.0), 4.0f/3.0f, 1.0f, 5.0f);
        int uniViewFrustum = glGetUniformLocation(program, "viewFrustum");
        glUniformMatrix4fv(uniViewFrustum, false, viewFrustum.getBuffer());
*/
        int uniModel = glGetUniformLocation(program, "model");

        int uniView = glGetUniformLocation(program, "view");
        glUniformMatrix4fv(uniView, false,
                Camera.LookAt(
                        new float[]{0f, 1.5f, 2.0f},
                        new float[]{0,0,0},
                        new float[]{0,0,1}
                        ).getBuffer());
        int uniProj = glGetUniformLocation(program, "proj");
        glUniformMatrix4fv(uniProj, false,
                Camera.perspective(
                (float)Math.toRadians(45), 800.0f/600.0f, 0.0f, 10.0f
                ).getBuffer());

        int posAttrib = glGetAttribLocation(program, "pos");
        glEnableVertexAttribArray(posAttrib);
        glVertexAttribPointer(posAttrib, 3, GL_FLOAT, false, 6 * 4, 0);

        int colAttrib = glGetAttribLocation(program, "color");
        glEnableVertexAttribArray(colAttrib);
        glVertexAttribPointer(colAttrib, 3, GL_FLOAT, false, 6 * 4, 3 * 4);

//        int uniTras = glGetUniformLocation(program, "tras");

        glEnable(GL_DEPTH_TEST);

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

//            glUniform3f(uniTras, 0.0f, -0.3f, 2.0f);

            glUniformMatrix4fv(uniModel, false,
                        FloatMatrix.getIdentityMatrix(4).getBuffer());
//                    FloatMatrix.getRotationZ((float)glfwGetTime() * 8.0f).translate(0,0,0).getBuffer());

            glDrawArrays(GL_POINTS, 0, 8);

            glfwSwapBuffers(window);

            glfwPollEvents();

        }
    }

    private void makePoints() {
        float[] points = {
                -0.45f,  0.25f, -0.45f, 1.0f, 0.0f, 0.0f,
                0.45f,  0.25f, -0.45f, 0.0f, 1.0f, 0.0f,
                0.45f, -0.25f, -0.45f, 0.0f, 0.0f, 1.0f,
                -0.45f, -0.25f, -0.45f, 1.0f, 1.0f, 0.0f,

                -0.45f,  0.45f,  0.45f, 0.0f, 1.0f, 1.0f,
                0.45f,  0.45f,  0.45f, 1.0f, 0.0f, 1.0f,
                0.45f, -0.45f,  0.45f, 1.0f, 0.5f, 0.5f,
                -0.45f, -0.45f,  0.45f, 0.5f, 1.0f, 0.5f

        };

        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, points, GL_STATIC_DRAW);
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
    public int makeProgram(String vertexShader, String fragShader, String geomShader) {

        int vertex_shader = makeShader(GL_VERTEX_SHADER, vertexShader);

        if (glGetShaderi(vertex_shader, GL_COMPILE_STATUS) == GL_FALSE){
            System.err.println(glGetShaderInfoLog(vertex_shader));
            return -1;
        }
        int fragment_shader = makeShader(GL_FRAGMENT_SHADER, fragShader);

        if (glGetShaderi(fragment_shader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println(glGetShaderInfoLog(fragment_shader));
            return -1;
        }
        int geometry_shader = makeShader(GL_GEOMETRY_SHADER, geomShader);
        if (glGetShaderi(geometry_shader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println(glGetShaderInfoLog(geometry_shader));
            return -1;
        }

        int shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertex_shader);
        glAttachShader(shaderProgram, fragment_shader);
        glAttachShader(shaderProgram, geometry_shader);

        glBindFragDataLocation(shaderProgram, 0, "outColor");

        glLinkProgram(shaderProgram);

        return shaderProgram;
    }
    public int makeShader(int type, String fileName) {
        int vertex_shader = glCreateShader(type);
        glShaderSource(vertex_shader, readFile("src/main/resources/geometry/" + fileName));
        glCompileShader(vertex_shader);

        return vertex_shader;
    }

    public static String readFile(String fileName) {
        try {
            return Files.readString(Path.of(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return Path.of(fileName).toString();
        }
    }

    public static void main (String [] args) {
        new GeometryShaders().run();
    }
}
