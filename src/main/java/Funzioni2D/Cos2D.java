package Funzioni2D;

public class Cos2D extends Funzione2D{
    @Override
    public float getY(float x) {
        return (float)Math.cos(x);
    }

    @Override
    public float getX(float t) {
        return t;
    }
}
