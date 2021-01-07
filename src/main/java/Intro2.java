import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.opengl.GL15.*;

import java.nio.FloatBuffer;

public class Intro2 {
    public static void main(String[] args){
        glfwInit();

        long window = createWindow();

        FloatBuffer buffer = memAllocFloat(3 * 2);
//        FloatBuffer buffer = memAllocFloat(6 * 2);

        buffer.put(-0.5f).put(-0.5f);
        buffer.put(0.5f).put(-0.5f);
        buffer.put(0.5f).put(0.5f);
/*
        buffer.put(1f).put(1f);
        buffer.put(0.5f).put(1f);
        buffer.put(0.5f).put(0.5f);
*/

        buffer.flip();

        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

        memFree(buffer);

        glEnableClientState(GL_VERTEX_ARRAY);
        glVertexPointer(2, GL_FLOAT, 0, 0L);
        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();
            glDrawArrays(GL_TRIANGLES, 0, 3);
//            glDrawArrays(GL_TRIANGLES, 0, 6);
            glfwSwapBuffers(window);
        }
        glfwTerminate();
    }

    private static long createWindow(){
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        long window = glfwCreateWindow(800, 600, "Intro2", NULL, NULL);

        glfwMakeContextCurrent(window);

        createCapabilities();

        return window;
    }

}

