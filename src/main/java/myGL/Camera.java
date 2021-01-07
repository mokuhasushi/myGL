package myGL;

public class Camera extends FloatMatrix{
    public Camera(float[][] buffer, int size) {
        super(buffer, size);
    }
    public Camera(float angleOfView, float aspectRatio, float zNear, float zFar) {
        super(new float[][] {
                {1.0f/(float)(Math.tan(angleOfView)), 0.0f, 0.0f, 0.0f},
                {0.0f, aspectRatio/(float)Math.tan(angleOfView),  0.0f ,0.0f},
                {0.0f, 0.0f, (zFar+zNear)/(zFar-zNear), 1.0f},
                {0.0f, 0.0f, -2.0f*zFar*zNear/(zFar-zNear), 0.0f}
        }, 4);
    }

    public static Camera LookAt (float[] eye, float[] center, float [] up) {
        float[] F = new float[3];
        for (int i = 0; i < 3; i++) {
            F[i] = center[i] - eye[i];
        }
        float [] f = normalize(F);
        float [] upN = normalize(up);

        float [] s = normalize(crossProduct(f, upN));

        float [] u = crossProduct(s, f);

        float[][] M = {
                {s[0], s[1], s[2], -eye[0]},
                {u[0], u[1], u[2], -eye[1]},
                {-f[0], -f[1], -f[2], -eye[2]},
                {0, 0, 0, 1}
        };

        return new Camera(M, 4);
    }

    public static Camera perspective (float angle, float ratio, float near, float far) {
        float tanHalfAngle = (float)Math.tan(angle/2);
        return new Camera(new float[][] {
                {1/(ratio * tanHalfAngle), 0, 0, 0},
                {0, 1/tanHalfAngle, 0, 0},
                {0, 0, -(far+near)/(far-near), -1},
                {0,0,-(2*far*near)/(far-near),0}
        }, 4);
    }

    @Override
    public Camera clone () {
        return new Camera(this.matrix.clone(), size);
    }

    public static void main (String [] args) {
        float [] idasv = new Camera((float)Math.toRadians(45.0), 4.0f/3.0f, 0.5f, 5.0f).getBuffer();
        for (int i = 0; i < 16; i++) {
            System.out.print(idasv[i] + " ");
        }

    }
}
