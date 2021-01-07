package Funzioni2D;

public class Sin2D extends Funzione2D{
    @Override
    public float getY(float x) {
        return (float) Math.sin(x);
    }

    @Override
    public float getX(float t) {
        return t;
    }
}
