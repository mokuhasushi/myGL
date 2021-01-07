package Funzioni2D;

import myGL.Coord2D;

public class Bezier2D  extends Funzione2D{
    private Coord2D[] P;
    int [] t;
    int [] tMinusOne;
    private int [] coefficient;

    public Bezier2D (Coord2D[] P) {
        this.P = new Coord2D[P.length];
        System.arraycopy(P, 0, this.P, 0, P.length);
        this.t = new int[P.length];
        this.tMinusOne = new int[P.length];
        this.coefficient = new int[P.length];
        solveBernstein();
/*
        for (int j = 0; j < P.length; j++) {
            System.out.println("t * " + t[j] + " + (t - 1) * " + tMinusOne[j] + " + P[i] * " + coefficient[j]);
        }
*/
    }

    public void solveBernstein() {
        int [] t = new int[P.length];
        int [] tMinusOne = new int[P.length];
        int [] coefficient = new int[P.length];

        int k = P.length -1;

        for (int i = 0; i < P.length; i++) {
            t[i] = i;
            tMinusOne[i] = k - i;
            coefficient[i] = binomial(k, i);
        }
        System.arraycopy(t, 0, this.t, 0, P.length);
        System.arraycopy(tMinusOne, 0, this.tMinusOne, 0, P.length);
        System.arraycopy(coefficient, 0, this.coefficient, 0, P.length);
    }

    public int binomial (int n, int k) {
        return factorial(n) / (factorial(k) * factorial(n-k));
    }

    public int factorial (int n) {
        if (n == 0)
            return 1;
        return factorial(n-1) * n;
    }

    /*
    public void solveDeCastelJeu() {
        int [] t = new int[P.length];
        int [] tMinusOne = new int[P.length];
        int [] coefficient = new int[P.length];

        int[][] ps = new int[P.length][];

        for (int i = 0; i < P.length; i++) {
            ps[i] = new int[i + 1];
        }

        for (int j = 0; j < P.length-1; j++) {
            tMinusOne[j]
        }
    }
*/

    @Override
    public float getY(float t) {
        float y = 0;

        for (int i = 0; i < P.length; i++) {
            y += (Math.pow(t, this.t[i]) * Math.pow(1-t, tMinusOne[i]) * coefficient[i]) * P[i].getY();
        }

        return y;
    }

    @Override
    public float getX(float t) {
        float x = 0;

        for (int i = 0; i < P.length; i++) {
            x += (Math.pow(t, this.t[i]) * Math.pow(1-t, tMinusOne[i]) * coefficient[i]) * P[i].getX();
        }

        return x;
    }
}
