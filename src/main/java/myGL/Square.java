package myGL;

public class Square {

    private Vertex[] vertices = new Vertex[4];

    public Square (float x, float y, float z, float size) {
        vertices[0] = new Vertex(x-size/2, y+size/2, z, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        vertices[1] = new Vertex(x+size/2, y+size/2, z, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f);
        vertices[2] = new Vertex(x+size/2, y-size/2, z, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        vertices[3] = new Vertex(x-size/2, y-size/2, z, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f);
    }
    public Square (float x, float y, float z, float size, float[] color) {
        vertices[0] = new Vertex(x-size/2, y+size/2, z, color[0], color[1], color[2], 0.0f, 0.0f);
        vertices[1] = new Vertex(x+size/2, y+size/2, z, color[0], color[1], color[2], 1.0f, 0.0f);
        vertices[2] = new Vertex(x+size/2, y-size/2, z, color[0], color[1], color[2], 1.0f, 1.0f);
        vertices[3] = new Vertex(x-size/2, y-size/2, z, color[0], color[1], color[2], 0.0f, 1.0f);
    }
    public Square (float x, float y, float z, float size, String plane) {
        if (plane.equals("X")) {
            vertices[0] = new Vertex(x, y + size / 2, z - size / 2, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f);
            vertices[1] = new Vertex(x, y + size / 2, z + size / 2, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f);
            vertices[2] = new Vertex(x, y - size / 2, z + size / 2, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
            vertices[3] = new Vertex(x, y - size / 2, z - size / 2, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f);
        }
        else if (plane.equals("Y")){
            vertices[0] = new Vertex(x - size / 2, y, z + size / 2, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f);
            vertices[1] = new Vertex(x + size / 2, y, z + size / 2, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f);
            vertices[2] = new Vertex(x + size / 2, y, z - size / 2, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
            vertices[3] = new Vertex(x - size / 2, y, z - size / 2, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f);
        }
        else {
            vertices[0] = new Vertex(x - size / 2, y + size / 2, z, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f);
            vertices[1] = new Vertex(x + size / 2, y + size / 2, z, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f);
            vertices[2] = new Vertex(x + size / 2, y - size / 2, z, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
            vertices[3] = new Vertex(x - size / 2, y - size / 2, z, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f);
        }
    }
    public Square (float x, float y, float z, float size, float [] color,  String plane) {
        if (plane.equals("X")) {
            vertices[0] = new Vertex(x, y + size / 2, z - size / 2, color[0], color[1], color[2], 0.0f, 0.0f);
            vertices[1] = new Vertex(x, y + size / 2, z + size / 2, color[0], color[1], color[2], 1.0f, 0.0f);
            vertices[2] = new Vertex(x, y - size / 2, z + size / 2, color[0], color[1], color[2], 1.0f, 1.0f);
            vertices[3] = new Vertex(x, y - size / 2, z - size / 2, color[0], color[1], color[2], 0.0f, 1.0f);
        }
        else if (plane.equals("Y")){
            vertices[0] = new Vertex(x - size / 2, y, z + size / 2, color[0], color[1], color[2], 0.0f, 0.0f);
            vertices[1] = new Vertex(x + size / 2, y, z + size / 2, color[0], color[1], color[2], 1.0f, 0.0f);
            vertices[2] = new Vertex(x + size / 2, y, z - size / 2, color[0], color[1], color[2], 1.0f, 1.0f);
            vertices[3] = new Vertex(x - size / 2, y, z - size / 2, color[0], color[1], color[2], 0.0f, 1.0f);
        }
        else {
            vertices[0] = new Vertex(x - size / 2, y + size / 2, z, color[0], color[1], color[2], 0.0f, 0.0f);
            vertices[1] = new Vertex(x + size / 2, y + size / 2, z, color[0], color[1], color[2], 1.0f, 0.0f);
            vertices[2] = new Vertex(x + size / 2, y - size / 2, z, color[0], color[1], color[2], 1.0f, 1.0f);
            vertices[3] = new Vertex(x - size / 2, y - size / 2, z, color[0], color[1], color[2], 0.0f, 1.0f);
        }
    }

    public Square (Vertex[] vertices) {
        vertices = vertices.clone();
    }

    public float[] getBuffer() {
        float[] ret = new float[4*8];
        for (int i = 0; i < 4; i++) {
            System.arraycopy(vertices[i].getBuffer(), 0, ret, i*8, 8);
        }
        return ret;
    }

    public float[] getBufferArray() {
        float[] ret = new float[6*8];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(vertices[i].getBuffer(), 0, ret, i*8, 8);
        }
        for (int i = 2; i < 5; i++) {
            if (i == 4)
                System.arraycopy(vertices[0].getBuffer(), 0, ret, (i+1)*8, 8);
            else
                System.arraycopy(vertices[i].getBuffer(), 0, ret, (i+1)*8, 8);
        }
        return ret;
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    public void setVertices(Vertex[] vertices) {
        this.vertices = vertices;
    }
}
