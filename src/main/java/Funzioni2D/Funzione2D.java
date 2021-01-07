package Funzioni2D;

public abstract class Funzione2D {
    public abstract float getY(float t);
    public abstract float getX(float t);
    public float[][] getSamples(float start, float end, int resolution) {
        float[][] samples = new float[2][resolution];
        float range = end - start;
        float step = range / (resolution - 1);
        for (int i = 0; i < resolution; i++) {
            float t = start + i * step;
            samples[0][i] = getX(t);
            samples[1][i] = getY(t);
        }
        return samples;
    }

}
