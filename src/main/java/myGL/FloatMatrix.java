package myGL;

public class FloatMatrix{

    float[][] matrix;
    int size;

    public static void main (String [] args) {
        FloatMatrix id = new FloatMatrix(new float[][]{{1.0f, 0.0f, 0.0f}, {0.0f, 1.0f, 0.0f}, {0.0f, 0.0f, 1.0f}}, 3);
        FloatMatrix a = new FloatMatrix(new float[][]{{3.0f, 2.0f, 1.0f}, {3.0f, 2.0f, 1.0f}, {3.0f, 2.0f, 1.0f}}, 3);
        System.out.println(id);
        System.out.println(a);
        System.out.println(id.sum(a));
        System.out.println(id.product(a));
        System.out.println(a.product(a));
        System.out.println(getRotationZ(180));
        float [] idasv = id.getBuffer();
        for (int i = 0; i < 9; i++) {
            System.out.print(idasv[i] + " ");
        }
        System.out.println("");
        System.out.println(getScaleMatrix(1,1,-1));
        System.out.println(getTranslationMatrix(1,1,-1));
        System.out.println(getTranslationMatrix(1, 2, 3).product(getScaleMatrix(2, 2, 2)));
    }

    public FloatMatrix (float [][] buffer, int size) {
        this.matrix = buffer;
        this.size = size;
    }

    public FloatMatrix sum(FloatMatrix other) {
        float [][] ret = new float[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j <size; j++) {
                ret[i][j] = this.getElement(i, j) + other.getElement(i, j);
            }
        }
        return new FloatMatrix(ret, size);
    }

    public FloatMatrix product(FloatMatrix other) {
        float [][] ret = new float[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                ret[i][j] = 0.0f;
                for (int k = 0; k < size; k++) {
                    ret[i][j] += this.getElement(i, k) * other.getElement(k, j);
                }
            }
        }
        return new FloatMatrix(ret, size);
    }

    public float[] product(float[] vector) {
        if (size != vector.length)
            return null;
        float[] ret = new float[size];
        for (int i = 0; i < size; i++) {
            ret[i] = 0;
            for (int j = 0; j < size; j++) {
                ret[i] += this.getElement(i, j) * vector[j];
            }
        }
        return ret;
    }

    public float[] getRow (int i) {
        return matrix[i];
    }
    public void setRow (float[] row, int i) {
        matrix[i] = row;
    }

    public float[] getColumn (int j) {
        return this.traspose().getRow(j);
    }
    public void setColumn (float[] column, int j) {
        for (int i = 0; i < size; j++) {
            matrix[i][j] = column[i];
        }
    }

    public float getElement(int i, int j) {
        return matrix[i][j];
    }
    public void setElement(float val, int i, int j) {
        matrix[i][j] = val;
    }

    public float getSize () {
        return size;
    }

    public float[][] getMatrix() {
        return matrix;
    }

    public float[] getBuffer() {
        float [] ret = new float[size*size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(matrix[i], 0, ret, i * size, size);
        }
        return ret;
    }

    public void setMatrix(float[][] matrix, int size) {
        this.matrix = matrix;
        this.size = size;
    }


    public FloatMatrix traspose() {
        float [][] ret = new float[size][size];
        for (int j = 0; j < size; j++) {
            for (int i = 0; i < size; i++) {
                ret[i][j] = matrix[j][i];
            }
        }

        return new FloatMatrix(ret, size);
    }

    public FloatMatrix scalarProduct(float a) {
        float [][] ret = new float[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                ret[i][j] = matrix[i][j] * a;
            }
        }
        return new FloatMatrix(ret, size);
    }

    public static FloatMatrix getIdentityMatrix(int size) {
        float[][] ret = new float[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j)
                    ret[i][j] = 1.0f;
                else
                    ret[i][j] = 0.0f;
            }
        }
        return new FloatMatrix(ret, size);
    }

    public static FloatMatrix getRotationX (float degrees) {
        double t = Math.toRadians(degrees);
        return new FloatMatrix(new float[][]{
                {1.0f, 0.0f, 0.0f, 0.0f},
                {0.0f, (float)Math.cos(t), (float)-Math.sin(t), 0.0f},
                {0.0f, (float)Math.sin(t), (float)Math.cos(t), 0.0f},
                {0.0f, 0.0f, 0.0f, 1.0f}
        }, 4);
    }

    public static FloatMatrix getRotationY (float degrees) {
        double t = Math.toRadians(degrees);
        return new FloatMatrix(new float[][]{
                {(float)Math.cos(t), 0.0f, (float)Math.sin(t), 0.0f},
                {0.0f, 1.0f, 0.0f, 0.0f},
                {(float)-Math.sin(t), 0.0f, (float)Math.cos(t), 0.0f},
                {0.0f, 0.0f, 0.0f, 1.0f}
        }, 4);
    }

    public static FloatMatrix getRotationZ (float degrees) {
        double t = Math.toRadians(degrees);
        return new FloatMatrix(new float[][]{
                {(float)Math.cos(t), (float)-Math.sin(t), 0.0f, 0.0f},
                {(float)Math.sin(t), (float)Math.cos(t), 0.0f, 0.0f},
                {0.0f, 0.0f, 1.0f, 0.0f},
                {0.0f, 0.0f, 0.0f, 1.0f}
        }, 4);
    }

    public static FloatMatrix getTranslationMatrix (float x, float y, float z) {
        return new FloatMatrix(new float[][]{
                {1.0f, 0.0f, 0.0f, x},
                {0.0f, 1.0f, 0.0f, y},
                {0.0f, 0.0f, 1.0f, z},
                {0.0f, 0.0f, 0.0f, 1.0f}
        }, 4);
    }

    public static FloatMatrix getScaleMatrix (float sx, float sy, float sz) {
        FloatMatrix id  = getIdentityMatrix(4);
        id.setElement(sx, 0,0);
        id.setElement(sy, 1,1);
        id.setElement(sz, 2,2);

        return id;
    }

    public static float[] normalize (float[] vector) {
        float norm = 0;
        for (int i = 0; i < vector.length; i++) {
            norm += Math.pow(vector[i], 2);
        }
        norm = (float)Math.sqrt(norm);

        float [] ret = new float[vector.length];

        for (int i = 0; i < vector.length; i++) {
            ret[i] = vector[i] / norm;
        }
        return ret;
    }

    public static float[] crossProduct (float[] a, float[] b) {
        if (a.length != 3 || b.length != 3)
            return null;
        float [] ret = new float[3];
        ret[0] = a[1]*b[2] - a[2]*b[1];
        ret[1] = a[2]*b[0] - a[0]*b[2];
        ret[2] = a[0]*b[1] - a[1]*b[0];

        return ret;
    }

    public FloatMatrix translate (float x, float y, float z) {
        this.matrix = FloatMatrix.getTranslationMatrix(x, y, z).product(this).getMatrix();
        return this;
/*
        this.setElement(this.getElement(0, 3) + x,0, 3);
        this.setElement(this.getElement(1, 3) + y,1, 3);
        this.setElement(this.getElement(2, 3) + z,2, 3);
        return this;
*/
    }

    public FloatMatrix scale (float sx, float sy, float sz) {
        this.setElement(this.getElement(0, 0) * sx,0, 0);
        this.setElement(this.getElement(1, 1) * sy,1, 1);
        this.setElement(this.getElement(2, 2) * sz,2, 2);
        return this;
    }

    public FloatMatrix rotate (float rx, float ry, float rz) {
        FloatMatrix RX = getRotationX(rx);
        FloatMatrix RY = getRotationY(ry);
        FloatMatrix RZ = getRotationZ(rz);

        this.matrix = this.product(RX).product(RY).product(RZ).getMatrix();

        return this;
    }

    public static FloatMatrix ROTATION902x2 = new FloatMatrix(new float[][] {
            {0, 1},
            {1, 0}
    }, 2);

    @Override
    public FloatMatrix clone () {
        return new FloatMatrix(this.matrix.clone(), size);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                sb.append(String.format("%.2f   ",matrix[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
