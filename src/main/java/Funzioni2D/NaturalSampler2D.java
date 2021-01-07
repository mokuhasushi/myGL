package Funzioni2D;

public class NaturalSampler2D extends Sampler2D{
    public NaturalSampler2D(Funzione2D func, int resolution, float range) {
        super(func, resolution);
    }

    @Override
    public float[][] getSamples(float start, float end){
        float[][] samples = new float[2][resolution];
        float range = end - start;
        float step = range / (resolution - 1);
        for (int i = 0; i < resolution; i++) {
            samples[0][i] = (float) Math.cos(i*step);
            samples[1][i] = (float) Math.sin(i*step);
        }
        return samples;
    }
}
