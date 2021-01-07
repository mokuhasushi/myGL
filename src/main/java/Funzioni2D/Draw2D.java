package Funzioni2D;

import myGL.Coord2D;
import myGL.FloatMatrix;
import myGL.GLApp;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.system.MemoryUtil.memAllocFloat;

public class Draw2D extends GLApp {

    private final int RESOLUTION = 100;
    private final int AXIS_LENGTH = 4;
    private final int FUNC_LENGTH = (RESOLUTION * 2 - 2);
    private final int VERTEX_LENGTH = 5;

    @Override
    public void loop(int program, long window) {
        int uniModel = glGetUniformLocation(program, "model");

        int posAttrib = glGetAttribLocation(program, "pos");
        glEnableVertexAttribArray(posAttrib);
        glVertexAttribPointer(posAttrib, 2, GL_FLOAT, false, 5 * 4, 0);

        int colAttrib = glGetAttribLocation(program, "color");
        glEnableVertexAttribArray(colAttrib);
        glVertexAttribPointer(colAttrib, 3, GL_FLOAT, false, 5 * 4, 2 * 4);

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        FloatMatrix model = FloatMatrix.getIdentityMatrix(4);

        setUpSimpleCameraMovement2D(model, window);

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT);

            glUniformMatrix4fv(uniModel, true,
                    model.getBuffer());

            drawAxis();
            drawFunc(4);

            glfwSwapBuffers(window);

            glfwPollEvents();

        }
    }

    @Override
    public void setUpScene() {
        float[] axis = setAxis();
//        float[] sin = setFunc(new Sin2D(), -10, 10, new float[] {1, 0, 0});
        float[] parabola = setFunc(new Parabola2D(1, 0, 0), -10, 10, new float[] {0, 1, 0});
//        float[] cos = setFunc(new Cos2D(), 30, new float[]{0, 0, 1});
        float[] bezier1 = setFunc(new BezierK22D(new Coord2D(5,5), new Coord2D(3,0), new Coord2D(-1, 1)),
                0, 1, new float[]{0,0,1});
        float[] circum = setFunc(new Circumference2D(2),0, (float) (2 * Math.PI), new float[] {1, 1, 0});
//        float[] circum = setFuncN((float) (2 * Math.PI), new float[] {1, 1, 0});

        float[] bezier2 = setFunc(new Bezier2D(new Coord2D[] {
                        new Coord2D(-5,5f),new Coord2D(-3,-2.0f),
                        new Coord2D(-1,1.1f), new Coord2D(1,-1.1f),
                        new Coord2D(3,0f), new Coord2D(5,-5f)}),
                0, 1, new float[]{1, 0, 0}
                );
        FloatBuffer fb = memAllocFloat((AXIS_LENGTH + FUNC_LENGTH*4) * VERTEX_LENGTH);
        fb.put(axis);
        fb.put(parabola);
        fb.put(bezier2);
        fb.put(bezier1);
        fb.put(circum);
        fb.flip();

        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, fb, GL_STATIC_DRAW);
    }

    public float[] setFuncN(float start, float end, float[] color) {
        Sampler2D func = new NaturalSampler2D(null, RESOLUTION, end - start);
        float[][] samples = func.getSamples(start, end);
        float[] points = samplesToPoints(samples, color);
        return points;
    }
/*
    public float[] setFunc(Funzione2D funzione2D, float start, float end, float[] color) {
        Sampler2D func = new Sampler2D(funzione2D, RESOLUTION);
        float[][] samples = func.getSamples(start, end);
        float[] points = samplesToPoints(samples, color);
        return points;
    }
*/
    public float[] setFunc(Funzione2D func, float start, float end, float[] color) {
        float[][] samples = func.getSamples(start, end, RESOLUTION);
        float[] points = samplesToPoints(samples, color);
        return points;
    }

    public float[] samplesToPoints(float [][] samples, float[] color) {
        float[] points = new float[FUNC_LENGTH * VERTEX_LENGTH];
        int i = 0;
        for (int j = 0; j < RESOLUTION; j++) {
            if (i == 0 || i == FUNC_LENGTH - 1) {
                points[5*i] = samples[0][j];
                points[5*i + 1] = samples[1][j];
                points[5*i + 2] = color[0];
                points[5*i + 3] = color[1];
                points[5*i + 4] = color[2];
            } else {
                points[5*i] = samples[0][j];
                points[5*i + 1] = samples[1][j];
                points[5*i + 2] = color[0];
                points[5*i + 3] = color[1];
                points[5*i + 4] = color[2];
                i++;
                points[5*i] = samples[0][j];
                points[5*i + 1] = samples[1][j];
                points[5*i + 2] = color[0];
                points[5*i + 3] = color[1];
                points[5*i + 4] = color[2];
            }
            i++;
        }

        return points;
    }

    public float[] setAxis(){
        float[] axis = {
                -50,  0, 1, 1, 1,
                 50,  0, 1, 1, 1,
                 0, -50, 1, 1, 1,
                 0,  50, 1, 1, 1
        };

        return axis;
    }
    public void drawAxis(){
        glDrawArrays(GL_LINES, 0, 2);
        glDrawArrays(GL_LINES, 2, 2);
    }
    public void drawFunc(int nFunc){
        for (int i = 0; i < nFunc; i++) {
            glDrawArrays(GL_LINES, 4 + i * FUNC_LENGTH, FUNC_LENGTH);
        }
    }

    @Override
    public void setUpApp() {
        this.appName = "Prova di disegno di funzioni 2d";
        this.dir = "funzioni2D";
        this.fragmentShaderSource = "func2D.frag";
        this.vertexShaderSource = "func2D.vert";
        this.geometryShaderSource = "";
    }

    public static void main (String [] args) {
        new Draw2D().run();
    }

}
