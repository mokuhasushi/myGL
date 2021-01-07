package myGL;

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
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetAttribLocation;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.glBindFragDataLocation;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;

public abstract class GLApp {

    public String dir = "";
    public String vertexShaderSource = "", geometryShaderSource = "", fragmentShaderSource = "";
    public String appName = "GL APP!";
    public int windowWidth = 800;
    public int windowHeight = 600;

    public final float [] RED = new float[]{1, 0, 0};
    public final float [] GREEN = new float[]{0, 1, 0};
    public final float [] BLUE = new float[]{0, 0, 1};
    public final float [] WHITE = new float[]{1, 1, 1};
    public final float [] BLACK = new float[]{0, 0, 0};
    public final float [] YELLOW = new float[]{1, 1, 0};

    public void run () {
        setUpApp();

        System.out.println(this.appName + " " + Version.getVersion() + "!");

        System.out.println("App set up done!");

        long window = init();

        System.out.println("Window created!");

        int program = makeProgram(vertexShaderSource, fragmentShaderSource, geometryShaderSource);
        if (program < 0)
        {
            System.err.println("ERROR IN SHADERS");
            return;
        }
        glUseProgram(program);

        System.out.println("Program created!");

        setUpScene();

        System.out.println("Scene set up!");

        loop(program, window);

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();

    }

    public abstract void loop(int program, long window);
/*        int uniModel = glGetUniformLocation(program, "model");

        setView(program, new float[]{0f, 1.5f, 2.0f}, new float[]{0,0,0},new float[]{0,0,1});

        setProj(program, 45, 800.0f/600.0f, 0.0f, 10.0f);

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

        }*/


    public void setProj(int program, float angle, float ratio, float near, float far) {
        int uniProj = glGetUniformLocation(program, "proj");
        glUniformMatrix4fv(uniProj, false,
                Camera.perspective(
                        (float)Math.toRadians(angle), ratio, near, far
                ).getBuffer());
    }

    public void setView(int program, float[] eye, float[] center, float[] up) {
        int uniView = glGetUniformLocation(program, "view");
        glUniformMatrix4fv(uniView, true,
                Camera.LookAt(
                        eye,
                        center,
                        up
                ).getBuffer());
    }

    public void makePoints() {
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

    public long init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        long window = glfwCreateWindow(windowWidth, windowHeight, "Hello World!", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, (window_, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window_, true);
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


        int vao = glGenVertexArrays();
        glBindVertexArray(vao);

        return window;
    }

    public int makeProgram(String vertexShader, String fragShader, String geomShader) {

        int shaderProgram = glCreateProgram();

        int vertex_shader = makeShader(GL_VERTEX_SHADER, vertexShader);

        if (glGetShaderi(vertex_shader, GL_COMPILE_STATUS) == GL_FALSE){
            System.err.println(glGetShaderInfoLog(vertex_shader));
            return -1;
        }
        glAttachShader(shaderProgram, vertex_shader);

        int fragment_shader = makeShader(GL_FRAGMENT_SHADER, fragShader);

        if (glGetShaderi(fragment_shader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println(glGetShaderInfoLog(fragment_shader));
            return -1;
        }
        glAttachShader(shaderProgram, fragment_shader);

        int geometry_shader = 0;
        if (!geomShader.equals("")) {
            geometry_shader = makeShader(GL_GEOMETRY_SHADER, geomShader);
            if (glGetShaderi(geometry_shader, GL_COMPILE_STATUS) == GL_FALSE) {
                System.err.println(glGetShaderInfoLog(geometry_shader));
                return -1;
            }
            glAttachShader(shaderProgram, geometry_shader);
        }


        glBindFragDataLocation(shaderProgram, 0, "outColor");

        glLinkProgram(shaderProgram);

        return shaderProgram;
    }
    public int makeShader(int type, String fileName) {
        int vertex_shader = glCreateShader(type);
        glShaderSource(vertex_shader, readFile("src/main/resources/" + dir + "/" +  fileName));
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

    /**
     * Define Shader names
     public String dir = "";
     public String vertexShaderSource = "", geometryShaderSource = "", fragmentShaderSource = "";
     public String appName = "GL APP!";
     public int windowWidth = 800;
     public int windowHeight = 600;
     */
    public abstract void setUpApp();
    public abstract void setUpScene();
    public void setUpSimpleCameraMovement2D(FloatMatrix model, long window) {
        glfwSetKeyCallback(window, (window_, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_A/* && action == GLFW_RELEASE */)
                model.translate(0.1f, 0, 0);
            if ( key == GLFW_KEY_W/* && action == GLFW_RELEASE */)
                model.translate(0, -0.1f, 0);
            if ( key == GLFW_KEY_D/* && action == GLFW_RELEASE */)
                model.translate(-0.1f, 0, 0);
            if ( key == GLFW_KEY_S/* && action == GLFW_RELEASE */)
                model.translate(0, 0.1f, 0);
            if ( key == GLFW_KEY_Q/* && action == GLFW_RELEASE */)
                model.scale(0.9f, 0.9f, 0);
            if ( key == GLFW_KEY_E/* && action == GLFW_RELEASE */)
                model.scale(1.1f, 1.1f, 0);
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window_, true);
        });
    }
    public void setUpSimpleCameraMovement3D(FloatMatrix model, long window) {
        glfwSetKeyCallback(window, (window_, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_A/* && action == GLFW_RELEASE */)
                model.translate(0.1f, 0, 0);
            if ( key == GLFW_KEY_W/* && action == GLFW_RELEASE */)
                model.translate(0, -0.1f, 0);
            if ( key == GLFW_KEY_D/* && action == GLFW_RELEASE */)
                model.translate(-0.1f, 0, 0);
            if ( key == GLFW_KEY_S/* && action == GLFW_RELEASE */)
                model.translate(0, 0.1f, 0);
            if ( key == GLFW_KEY_F/* && action == GLFW_RELEASE */)
                model.translate(0, 0, -0.1f);
            if ( key == GLFW_KEY_C/* && action == GLFW_RELEASE */)
                model.translate(0, 0, 0.1f);
            if ( key == GLFW_KEY_Q/* && action == GLFW_RELEASE */) {
                model.scale(0.9f, 0.9f, 0.9f);
            }
            if ( key == GLFW_KEY_E/* && action == GLFW_RELEASE */)
            {
                model.scale(1.1f, 1.1f, 1.1f);
            }
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window_, true);
        });
    }

}
