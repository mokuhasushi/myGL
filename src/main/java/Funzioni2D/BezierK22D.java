package Funzioni2D;

import myGL.Coord2D;

public class BezierK22D extends Funzione2D{
    private Coord2D [] P = new Coord2D[3];

    public BezierK22D(Coord2D p0, Coord2D p1, Coord2D p2) {
        P[0] = p0;
        P[1] = p1;
        P[2] = p2;
    }

    @Override
    public float getY(float t) {
        return (1-t) * (1-t) * P[0].getY() + 2 * (1-t) * t * P[1].getY() + t * t * P[2].getY();
    }

    @Override
    public float getX(float t) {
        return (1-t) * (1-t) * P[0].getX() + 2 * (1-t) * t * P[1].getX() + t * t * P[2].getX();
    }

    @Override
    public float [][] getSamples(float start, float end, int resolution) {
        if (end - start > 1)
            return super.getSamples(0, 1, resolution);
        else
            return super.getSamples(0, end - start, resolution);
    }
}
