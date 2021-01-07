package introToModernOpenGL;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_SHORT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;

public class HelloWorldCh3GL {

    private long window;
    private HelloWorldGL.GResources gResources;
    private static double seconds = 0;


    public void run() {

        init();

        if (!makeResources("view-frustum-rotation.vert", "cubes.frag")) {
            System.err.println("Failed to load resources\n");
            return;
        }

        loop();

    }

    public boolean makeResources(String vertexShaderFileName, String fragShaderFileName) {
        gResources = new HelloWorldGL.GResources();
        FloatBuffer gVertexBufferData = memAllocFloat(4*4);
        gVertexBufferData
                .put(-1.0f).put(-1.0f).put(0.0f).put(1.0f)
                .put(1.0f).put(-1.0f).put(0.0f).put(1.0f)
                .put(-1.0f).put(1.0f).put(0.0f).put(1.0f)
                .put(1.0f).put(1.0f).put(0.0f).put(1.0f);
        gVertexBufferData.flip();
        IntBuffer gElementBufferData = memAllocInt(4);
        gElementBufferData
                .put(0)
                .put(1)
                .put(2)
                .put(3);
        gElementBufferData.flip();
        gResources.setVertex_buffer(makeBuffer(
                GL_ARRAY_BUFFER,
                gVertexBufferData,
                4 * 4
        ));
        gResources.setElement_buffer(makeBuffer(
                GL_ELEMENT_ARRAY_BUFFER,
                gElementBufferData,
                4
        ));
        memFree(gVertexBufferData);
        memFree(gElementBufferData);

        gResources.setTextures(new int[]{make_texture((short)255), make_texture((short)127)});

        String vertexShader = Utils.readFile("src/main/resources/" + vertexShaderFileName);
        String fragShader = Utils.readFile("src/main/resources/" + fragShaderFileName);

        gResources.setVertex_shader(make_shader(
                GL_VERTEX_SHADER,
                vertexShader
        ));
        gResources.setFragment_shader(make_shader(
                GL_FRAGMENT_SHADER,
                fragShader
        ));
        gResources.setProgram(make_program(
                gResources.getVertex_shader(),
                gResources.getFragment_shader()
        ));

        gResources.addUniform("timer", glGetUniformLocation(gResources.getProgram(), "timer"));
        gResources.addUniform("texture0", glGetUniformLocation(gResources.getProgram(), "texture0"));
        gResources.addUniform("texture1", glGetUniformLocation(gResources.getProgram(), "texture1"));
//        gResources.addUniform("textures0", glGetUniformLocation(gResources.getProgram(), "textures0"));
//        gResources.addUniform("textures[1]", glGetUniformLocation(gResources.getProgram(), "textures[1]"));

        gResources.addAttribute("position", glGetAttribLocation(gResources.getProgram(), "position"));

        return true;
    }

    public void loop() {
        glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        while (!glfwWindowShouldClose(window)) {
            updateTimer();
            render();
        }
    }

    private void updateFadeFactor(){
        gResources.fade_factor = (float) (Math.sin((float)glfwGetTime() * 1f) * 0.5 + 0.5);
    }

    private void render(){
        glClear(GL_COLOR_BUFFER_BIT);

        glUseProgram(gResources.getProgram());

        glUniform1f(gResources.getUniform("timer"), (float) seconds);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, gResources.getTextures()[0]);
        glUniform1i(gResources.getUniform("texture0"), 0);

        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, gResources.getTextures()[1]);
        glUniform1i(gResources.getUniform("texture1"), 1);

        glBindBuffer(GL_ARRAY_BUFFER, gResources.getVertex_buffer());
        glVertexAttribPointer(
                gResources.getAttribute("position"),
                4,
                GL_FLOAT,
                false,
                0, //Maybe?
                0
        );
        glEnableVertexAttribArray(gResources.getAttribute("position"));

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, gResources.getElement_buffer());
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
/*
        glDrawElements(
                GL_TRIANGLE_STRIP,
                4,
                GL_SHORT,
                0
        );
*/

        glDisableVertexAttribArray(gResources.getAttribute("position"));

        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        window = createWindow();

        glfwMakeContextCurrent(window);

        glfwSwapInterval(1);

        glfwShowWindow(window);

        createCapabilities();


    }

    public static long createWindow() {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        long window = glfwCreateWindow(800, 600, "Hello World!", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, (_window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(_window, true);
        });

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    window,
                    (vidMode.width() - pWidth.get(0)) / 2,
                    (vidMode.height() - pHeight.get(0)) /2
            );
        }

        return window;
    }

    public int makeBuffer(int target, final IntBuffer bufferData, long buffer_size) {
        int buffer = glGenBuffers();
        glBindBuffer(target, buffer);
        glBufferData(target, bufferData, GL_STATIC_DRAW);
        return buffer;
    }
    public int makeBuffer(int target, final FloatBuffer bufferData, long buffer_size) {
        int buffer = glGenBuffers();
        glBindBuffer(target, buffer);
        glBufferData(target, bufferData, GL_STATIC_DRAW);
        return buffer;
    }
    public int make_texture(short color)
    {
        int texture;
        int width, height;

//TODO        ShortBuffer pixels = readTGA(filename);
//        ShortBuffer pixels = hopeThisWorksTexture(color);
        FloatBuffer pixels = hopeThisWorksTexture(1.0f);
//        FloatBuffer pixels = hopeThisWorksTexture((float)color/255);
        width = 128;
        height = 128;
        texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glTexImage2D(
                GL_TEXTURE_2D, 0,
                GL_RGBA8,
                width, height, 0,
                GL_RGBA8, GL_SHORT,
                pixels
        );
        memFree(pixels);
        return texture;
    }
    public int make_shader(int type, String source) {
        int length;
        int shader;
        int shader_ok;

        shader = glCreateShader(type);
        glShaderSource(shader, source);
        glCompileShader(shader);

        return shader;
    }

    public int make_program(int vertex_shader, int fragment_shader) {
        int program  = glCreateProgram();
        glAttachShader(program, vertex_shader);
        glAttachShader(program, fragment_shader);
        glLinkProgram(program);

        return program;
    }

    public static void main(String[] args) {
        new HelloWorldCh3GL().run();
    }

    private ShortBuffer hopeThisWorksTexture(short color) {
        ShortBuffer sb = memAllocShort(128*128*4);
        for (int i = 0; i < 128; i++) {
            for (int j = 0; j < 128; j++) {
                if (i < 56)
                    sb.put(color).put((short)0).put((short)0).put((short)255);
                else
                    sb.put((short)0).put(color).put((short)0).put((short)255);
            }
        }
        sb.flip();
        return sb;
    }
    private FloatBuffer hopeThisWorksTexture(float color) {
        FloatBuffer sb = memAllocFloat(128*128*4);
        for (int i = 0; i < 128; i++) {
            for (int j = 0; j < 128; j++) {
                if (i < 56)
                    sb.put(color).put(0.0f).put(0.0f).put(1.0f);
                else
                    sb.put(0.0f).put(color).put(0.0f).put(1.0f);
            }
        }
        sb.flip();
        return sb;
    }

    public static int updateTimer() {
        double now = glfwGetTime() ;//* 0.001;
        int elapsed = (int) (now - seconds);
        seconds = now;
        return elapsed;
    }

    public static ShortBuffer readTGA (String filename){
        return null;
    }


    static class GResources {
        int vertex_buffer, element_buffer;
        int [] textures = new int[2];
        int vertex_shader, fragment_shader, program;

        HashMap<String, Integer> uniforms = new HashMap<>();
        HashMap<String, Integer> attributes = new HashMap<>();

        float fade_factor = 1.0f;

        public HashMap<String, Integer> getAttributes() {
            return attributes;
        }

        public void setAttributes(HashMap<String, Integer> attributes) {
            this.attributes = attributes;
        }

        public int getVertex_buffer() {
            return vertex_buffer;
        }

        public void setVertex_buffer(int vertex_buffer) {
            this.vertex_buffer = vertex_buffer;
        }

        public int getElement_buffer() {
            return element_buffer;
        }

        public void setElement_buffer(int element_buffer) {
            this.element_buffer = element_buffer;
        }

        public int[] getTextures() {
            return textures;
        }

        public void setTextures(int[] textures) {
            this.textures = textures;
        }

        public void addUniform(String key, Integer value) {
            uniforms.put(key, value);
        }

        public void addAttribute(String key, Integer value) {
            attributes.put(key, value);
        }

        public int getVertex_shader() {
            return vertex_shader;
        }

        public void setVertex_shader(int vertex_shader) {
            this.vertex_shader = vertex_shader;
        }

        public int getFragment_shader() {
            return fragment_shader;
        }

        public void setFragment_shader(int fragment_shader) {
            this.fragment_shader = fragment_shader;
        }

        public int getProgram() {
            return program;
        }

        public void setProgram(int program) {
            this.program = program;
        }

        public HashMap<String, Integer> getUniforms() {
            return uniforms;
        }

        public void setUniforms(HashMap<String, Integer> uniforms) {
            this.uniforms = uniforms;
        }

        public int getUniform(String key) {
            return uniforms.get(key);
        }
        public int getAttribute(String key) {
            return attributes.get(key);
        }

        public float getFade_factor() {
            return fade_factor;
        }

        public void setFade_factor(float fade_factor) {
            this.fade_factor = fade_factor;
        }
    }

}


