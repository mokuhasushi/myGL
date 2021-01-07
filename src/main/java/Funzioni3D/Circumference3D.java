package Funzioni3D;

import myGL.FloatMatrix;

public class Circumference3D extends Curva3D {
    public float radius;
    public float angleX, angleY, angleZ;
    public FloatMatrix rotation;

    public Circumference3D(float radius, float angleX, float angleY, float angleZ) {
        this.radius = radius;
        this.angleX = angleX;
        this.angleY = angleY;
        this.angleZ = angleZ;

        this.rotation = FloatMatrix.getRotationX(angleX)
                .product(FloatMatrix.getRotationY(angleY))
                .product(FloatMatrix.getRotationZ(angleZ));
    }



    @Override
    public float getX(float t) {
        return 0;
    }

    @Override
    public float getY(float t) {
        return 0;
    }

    @Override
    public float getZ(float t) {
        return 0;
    }

    @Override
    public float[] getXYZ(float t) {
        float [] ret = new float[4];
        ret[0] = (float) Math.cos(t) * radius;
        ret[1] = (float) Math.sin(t) * radius;
        ret[2] = 0;
        ret[3] = 1;
        return rotation.product(ret);
    }
}
