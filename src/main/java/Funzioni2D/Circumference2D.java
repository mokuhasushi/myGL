package Funzioni2D;

public class Circumference2D extends Funzione2D{
    private float radius;

    public Circumference2D (float radius) {
        this.radius = radius;
    }

    @Override
    public float getY(float t) {
        return (float)Math.sin(t) * radius;
    }

    @Override
    public float getX(float t) {
        return (float)Math.cos(t) * radius;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
