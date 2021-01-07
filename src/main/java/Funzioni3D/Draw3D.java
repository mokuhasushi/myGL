package Funzioni3D;

import Funzioni2D.*;
import myGL.Coord2D;
import myGL.FloatMatrix;
import myGL.GLApp;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.GL_LINES;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.memAllocFloat;

public class Draw3D extends GLApp {

    private final int RESOLUTION = 100;
    private final int AXIS_LENGTH = 6;
    private final int FUNC_LENGTH = (RESOLUTION * 2 - 2);
    private final int VERTEX_LENGTH = 6;
    private int funcNumber = 0;

    @Override
    public void loop(int program, long window) {
        int uniModel = glGetUniformLocation(program, "model");

        setView(program, new float[]{1f, 1f, 1f}, new float[]{0,0,0},new float[]{0,0,1});

        setProj(program, 45, 800.0f/600.0f, 1.0f, 10.0f);


        int posAttrib = glGetAttribLocation(program, "pos");
        glEnableVertexAttribArray(posAttrib);
        glVertexAttribPointer(posAttrib, 3, GL_FLOAT, false, 6 * 4, 0);

        int colAttrib = glGetAttribLocation(program, "color");
        glEnableVertexAttribArray(colAttrib);
        glVertexAttribPointer(colAttrib, 3, GL_FLOAT, false, 6 * 4, 3 * 4);

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        FloatMatrix model = FloatMatrix.getIdentityMatrix(4).scale(0.3f, 0.3f, 0.3f).translate(-1.9f,-0.5f,-0);

        cameraMovement3D(model, window);
        glEnable(GL_DEPTH_TEST);

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glUniformMatrix4fv(uniModel, true,
                    model.getBuffer());

            drawAxis();
            drawFunc(funcNumber);

            glfwSwapBuffers(window);

            glfwPollEvents();
        }
    }

    @Override
    public void setUpScene() {
        float[] axis = setAxis();
        funcNumber = 101;
        FloatBuffer fb = memAllocFloat((AXIS_LENGTH + FUNC_LENGTH*funcNumber) * VERTEX_LENGTH);

        float[] circ = setFunc(new Circumference2D(1), 0, (float)Math.PI*2, RED);
//        float[] circ2 = setFunc(new Circumference3D(1, 90, 90, 0),
//                0, (float)Math.PI*2, GREEN);

        fb.put(axis);
        fb.put(circ);
        for (int i = 0; i < 100; i++) {
            fb.put(setFunc(new Circumference3D(1, 360/100*i, 00, 0),
                    0, (float)Math.PI*2, GREEN));
        }
//        fb.put(circ2);
        fb.flip();

        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, fb, GL_STATIC_DRAW);
    }

    public float[] setFunc(Funzione2D func, float start, float end, float[] color) {
        float[][] samples = func.getSamples(start, end, RESOLUTION);
        float[] points = samplesToPoints(samples, color);
        return points;
    }

    public float[] setFunc(Curva3D curve, float start, float end, float[] color) {
        float[][] samples = curve.getSamples(start, end, RESOLUTION);
        float[] points = samplesToPoints3D(samples, color);
        return points;
    }

    public float[] samplesToPoints3D(float [][] samples, float[] color) {
        float[] points = new float[FUNC_LENGTH * VERTEX_LENGTH];
        int i = 0;
        for (int j = 0; j < RESOLUTION; j++) {
            if (i == 0 || i == FUNC_LENGTH - 1) {
                System.arraycopy(samples[j], 0, points, 6*i, 3);
                System.arraycopy(color, 0, points, 6*i + 3, 3);
            } else {
                System.arraycopy(samples[j], 0, points, 6*i, 3);
                System.arraycopy(color, 0, points, 6*i + 3, 3);
                i++;
                System.arraycopy(samples[j], 0, points, 6*i, 3);
                System.arraycopy(color, 0, points, 6*i + 3, 3);
            }
            i++;
        }
        return points;
    }

    public float[] samplesToPoints(float [][] samples, float[] color) {
        float[] points = new float[FUNC_LENGTH * VERTEX_LENGTH];
        int i = 0;
        for (int j = 0; j < RESOLUTION; j++) {
            if (i == 0 || i == FUNC_LENGTH - 1) {
                points[6*i] = samples[0][j];
                points[6*i + 1] = samples[1][j];
                points[6*i + 2] = 0.0f;
                points[6*i + 3] = color[0];
                points[6*i + 4] = color[1];
                points[6*i + 5] = color[2];
            } else {
                points[6*i] = samples[0][j];
                points[6*i + 1] = samples[1][j];
                points[6*i + 2] = 0.0f;
                points[6*i + 3] = color[0];
                points[6*i + 4] = color[1];
                points[6*i + 5] = color[2];
                i++;
                points[6*i] = samples[0][j];
                points[6*i + 1] = samples[1][j];
                points[6*i + 2] = 0.0f;
                points[6*i + 3] = color[0];
                points[6*i + 4] = color[1];
                points[6*i + 5] = color[2];
            }
            i++;
        }

        return points;
    }

    public float[] setAxis(){
        float[] axis = {
                -50,  0,   0, 1, 1, 1,
                 50,  0,   0, 1, 1, 1,
                 0, -50,   0, 1, 1, 1,
                 0,  50,   0, 1, 1, 1,
                 0,   0, -50, 1, 1, 1,
                 0,   0,  50, 1, 1, 1
        };
/*
        float[] axis = {
                -50,  0,   0, 1, 0, 0,
                50,  0,   0, 1, 0, 0,
                0, -50,   0, 0, 1, 0,
                0,  50,   0, 0, 1, 0,
                0,   0, -50, 0, 0, 1,
                0,   0,  50, 0, 0, 1
        };
*/

        return axis;
    }
    public void drawAxis(){
        glDrawArrays(GL_LINES, 0, 2);
        glDrawArrays(GL_LINES, 2, 2);
        glDrawArrays(GL_LINES, 4, 2);
    }
    public void drawFunc(int nFunc){
        for (int i = 0; i < nFunc; i++) {
            glDrawArrays(GL_LINES, 6 + i * FUNC_LENGTH, FUNC_LENGTH);
        }
    }

    @Override
    public void setUpApp() {
        this.appName = "Prova di disegno di funzioni 3d";
        this.dir = "funzioni3D";
        this.fragmentShaderSource = "func3D.frag";
        this.vertexShaderSource = "func3D.vert";
        this.geometryShaderSource = "";
    }

    public void cameraMovement3D(FloatMatrix model, long window) {
        glfwSetKeyCallback(window, (window_, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_A/* && action == GLFW_RELEASE */)
                model.translate(0.1f, 0, 0);
            if ( key == GLFW_KEY_W/* && action == GLFW_RELEASE */)
                model.translate(0, -0.1f, 0);
            if ( key == GLFW_KEY_D/* && action == GLFW_RELEASE */)
                model.translate(-0.1f, 0, 0);
            if ( key == GLFW_KEY_S/* && action == GLFW_RELEASE */)
                model.translate(0, 0.1f, 0);
            if ( key == GLFW_KEY_Z/* && action == GLFW_RELEASE */)
                model.translate(0, 0, -0.1f);
            if ( key == GLFW_KEY_X/* && action == GLFW_RELEASE */)
                model.translate(0, 0, 0.1f);
            if ( key == GLFW_KEY_R/* && action == GLFW_RELEASE */) {
                model.scale(0.9f, 0.9f, 0.9f);
            }
            if ( key == GLFW_KEY_F/* && action == GLFW_RELEASE */)
            {
                model.scale(1.1f, 1.1f, 1.1f);
            }
            if ( key == GLFW_KEY_Q/* && action == GLFW_RELEASE */)
                model.rotate(0f, 0, 1);
            if ( key == GLFW_KEY_E/* && action == GLFW_RELEASE */)
                model.rotate(0, 0f, -1);
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window_, true);
        });
    }

    public static void main (String [] args) {
        new Draw3D().run();
    }

}
