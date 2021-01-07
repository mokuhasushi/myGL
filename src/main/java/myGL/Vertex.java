package myGL;

public class Vertex {
    public float x, y, z;
    public float r, g, b;
    public float u, v;

    public Vertex () {}

    public Vertex(float x, float y, float z, float r, float g, float b, float u, float v) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.r = r;
        this.g = g;
        this.b = b;
        this.u = u;
        this.v = v;
    }

    public Vertex(float x, float y, float r, float g, float b) {
        this.x = x;
        this.y = y;
        this.z = 0;
        this.r = r;
        this.g = g;
        this.b = b;
        this.u = 0;
        this.v = 0;
    }

    public float[] getBuffer() {
        return new float[] {x, y, z, r, g, b, u, v};
    }

    public float[] getBufferXYRGB() {
        return new float[] {x, y, r, g, b};
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }

    public float getG() {
        return g;
    }

    public void setG(float g) {
        this.g = g;
    }

    public float getB() {
        return b;
    }

    public void setB(float b) {
        this.b = b;
    }

    public float getU() {
        return u;
    }

    public void setU(float u) {
        this.u = u;
    }

    public float getV() {
        return v;
    }

    public void setV(float v) {
        this.v = v;
    }
}
