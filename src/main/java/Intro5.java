import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.system.CallbackI;

public class Intro5 {

    private static void mouseCallback(long win, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            System.out.println("Pressed!");
        }
    }

    public static void main(String[] args) {
        glfwInit();
        long window = createWindow();

        glfwSetMouseButtonCallback(window, new GLFWMouseButtonCallbackI() {
            @Override
            public void invoke(long window, int button, int action, int mods) {

            }
        });

        glfwSetMouseButtonCallback(window, (long win, int button, int action, int mods)-> {

        });

        glfwSetMouseButtonCallback(window, Intro5::mouseCallback);

        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();
        }
        glfwTerminate();

    }

    private static long createWindow () {
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        long window  = glfwCreateWindow(800, 600, "Intro 5", NULL, NULL);

        glfwMakeContextCurrent(window);
        createCapabilities();
        return window;
    }
}
