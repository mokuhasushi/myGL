package Funzioni3D;

public abstract class Curva3D {
    public abstract float getX(float t);
    public abstract float getY(float t);
    public abstract float getZ(float t);
    public abstract float[] getXYZ (float t);
    public float[][] getSamples(float start, float end, int resolution) {
        float[][] samples = new float[resolution][3];
        float range = end - start;
        float step = range / (resolution - 1);
        for (int i = 0; i < resolution; i++) {
            float t = start + i * step;
            samples[i] = getXYZ(t);
        }
        return samples;
    }


}
