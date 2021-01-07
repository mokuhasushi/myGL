package Funzioni2D;

public class Sampler2D {
    Funzione2D func;
    int resolution;

    public Sampler2D(Funzione2D func, int resolution) {
        this.func = func;
        this.resolution = resolution;
    }

    public float[][] getSamples(float start, float end) {
        float[][] samples = new float[2][resolution];
        float range = end - start;
        float step = range / (resolution - 1);
        for (int i = 0; i < resolution; i++) {
            float t = start + i * step;
            samples[0][i] = func.getX(t);
            samples[1][i] = func.getY(t);
        }
        return samples;
    }

}
