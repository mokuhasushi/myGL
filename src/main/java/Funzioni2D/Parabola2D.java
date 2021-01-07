package Funzioni2D;

public class Parabola2D extends Funzione2D{
    float a, b, c;

    public Parabola2D(float a, float b, float c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public float getY(float x) {
        return a * x * x + b * x + c;
    }

    @Override
    public float getX(float t) {
        return t;
    }
}
