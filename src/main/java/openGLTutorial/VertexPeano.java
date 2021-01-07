package openGLTutorial;

import myGL.Vertex;

public class VertexPeano extends Vertex {
    float r00, r01, r10, r11;
    float edge;

    public VertexPeano (float x, float y, float r, float g, float b, float edge, float r00, float r01, float r10, float r11) {
        super(x, y, 0, r, g, b, 0, 0);
        this.r00 = r00;
        this.r01 = r01;
        this.r10 = r10;
        this.r11 = r11;
        this.edge = edge;
    }

    @Override
    public float[] getBuffer () {
        return new float[] {
                x, y,
                r, g, b,
                r00, r01,
                r10, r11
        };
    }

    public float getR00() {
        return r00;
    }

    public void setR00(float r00) {
        this.r00 = r00;
    }

    public float getR01() {
        return r01;
    }

    public void setR01(float r01) {
        this.r01 = r01;
    }

    public float getR10() {
        return r10;
    }

    public void setR10(float r10) {
        this.r10 = r10;
    }

    public float getR11() {
        return r11;
    }

    public void setR11(float r11) {
        this.r11 = r11;
    }

    public float getEdge() {
        return edge;
    }

    public void setEdge(float edge) {
        this.edge = edge;
    }
}
