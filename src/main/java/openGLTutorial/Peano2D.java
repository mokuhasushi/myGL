package openGLTutorial;

import myGL.Coord2D;
import myGL.FloatMatrix;
import myGL.GLApp;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.memAllocFloat;
import static org.lwjgl.system.MemoryUtil.memFree;

public class Peano2D extends GLApp {
    public static void main (String [] args) {
        new Peano2D().run();
    }
    private int level = 3;

    @Override
    public void setUpApp() {
        this.appName = "Peano2d";
        this.dir = "peano2D";
        this.vertexShaderSource = "peano.vert";
        this.fragmentShaderSource = "peano.frag";
        this.geometryShaderSource = "peano.geom.glsl";
        this.windowWidth = 800;
        this.windowHeight = 600;

    }

    @Override
    public void setUpScene() {
        FloatBuffer points = memAllocFloat(9 *  (int)Math.pow(9, level+1) * 4);

        peano(new Coord2D(-1f, 1f), 2, FloatMatrix.getIdentityMatrix(2), level, new float[] {1, 0, 0}, points);
        peano(new Coord2D(1f, 1f), 2, FloatMatrix.ROTATION902x2.scalarProduct(-1), level, new float[] {0, 1, 0}, points);
        peano(new Coord2D(1, -1), 2, FloatMatrix.getIdentityMatrix(2).scalarProduct(-1), level, new float[] {0, 0, 1}, points);
        peano(new Coord2D(-1, -1), 2, FloatMatrix.ROTATION902x2, level, new float[] {1, 1, 0}, points);

        points.flip();

        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, points, GL_STATIC_DRAW);

        memFree(points);
    }

    public Coord2D peano (Coord2D start, float edge, FloatMatrix rotation, int level, float[] color, FloatBuffer target){
        if (level == 0) {
            target.put(new float[]{start.getX(), start.getY(), color[0], color[1], color[2], edge,
                    rotation.getElement(0,0), rotation.getElement(0,1),
                    rotation.getElement(1,0), rotation.getElement(1,1)});
            return  new Coord2D(start.getX() + rotation.getElement(0, 0)*edge,
                                   start.getY() + rotation.getElement(0, 1)*edge);
        }
        else {
            Coord2D cur = start;

            float normalEdge = edge / 3.0f;


            cur = peano(cur, normalEdge, rotation, level - 1, color, target);
            cur = peano(cur, normalEdge, FloatMatrix.ROTATION902x2.product(rotation).scalarProduct(-1), level -1, color, target);
            cur = peano(cur, normalEdge, rotation, level - 1, color, target);
            cur = peano(cur, normalEdge, FloatMatrix.ROTATION902x2.product(rotation), level -1, color, target);
            cur = peano(cur, normalEdge, rotation.scalarProduct(-1), level - 1, color, target);
            cur = peano(cur, normalEdge, FloatMatrix.ROTATION902x2.product(rotation), level -1, color, target);
            cur = peano(cur, normalEdge, rotation, level - 1, color, target);
            cur = peano(cur, normalEdge, FloatMatrix.ROTATION902x2.product(rotation).scalarProduct(-1), level -1, color, target);
            cur = peano(cur, normalEdge, rotation, level - 1, color, target);

            return cur;
        }
    }

    @Override
    public void loop(int program, long window) {

        int uniModel = glGetUniformLocation(program, "model");

        int posAttrib = glGetAttribLocation(program, "pos");
        glEnableVertexAttribArray(posAttrib);
        glVertexAttribPointer(posAttrib, 2, GL_FLOAT, false, 10 * 4, 0);

        int colAttrib = glGetAttribLocation(program, "color");
        glEnableVertexAttribArray(colAttrib);
        glVertexAttribPointer(colAttrib, 3, GL_FLOAT, false, 10 * 4, 2 * 4);

        int edgeAttrib = glGetAttribLocation(program, "edge");
        glEnableVertexAttribArray(edgeAttrib);
        glVertexAttribPointer(edgeAttrib, 1, GL_FLOAT, false, 10 * 4, 5 * 4);

        int rotAttrib = glGetAttribLocation(program, "rotation2x2");
        glEnableVertexAttribArray(rotAttrib);
        glVertexAttribPointer(rotAttrib, 4, GL_FLOAT, false, 10 * 4, 6 * 4);

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        FloatMatrix model = FloatMatrix.getIdentityMatrix(4);

        setUpSimpleCameraMovement2D(model, window);

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT);

            glUniformMatrix4fv(uniModel, false,
                    model.getBuffer());

            glDrawArrays(GL_POINTS, 0, (int)Math.pow(9, level) * 4);

            glfwSwapBuffers(window);

            glfwPollEvents();

        }
    }

}
