package jo.vecmath.logic;

import jo.vecmath.Matrix3i;
import jo.vecmath.Matrix4i;
import jo.vecmath.Point3i;
import jo.vecmath.Tuple3f;
import jo.vecmath.Tuple3i;

public class Matrix4iLogic extends MathUtils {

    public static void rotateEuler(Matrix4i m, Tuple3f angles) {
        m.mul(makeRotateEulerMatrix(angles), m);
    }

    public static void rotateEuler(Matrix4i m, float x, float y, float z) {
        m.mul(makeRotateEulerMatrix(x, y, z), m);
    }

    public static void rotateEulerPre(Matrix4i m, Tuple3f angles) {
        m.mul(m, makeRotateEulerMatrix(angles));
    }

    public static void rotateEulerPre(Matrix4i m, float x, float y, float z) {
        m.mul(m, makeRotateEulerMatrix(x, y, z));
    }

    // angles in degrees
    public static Matrix4i makeRotateEulerMatrix(Tuple3f angles) {
        return makeRotateEulerMatrix(angles.x, angles.y, angles.z);
    }

    // angles in degrees
    public static Matrix4i makeRotateEulerMatrix(float x, float y, float z) {
        Matrix4i m = new Matrix4i();
        m.setIdentity();
        rotX(m, x * DEG_TO_RAD);
        rotY(m, y * DEG_TO_RAD);
        rotZ(m, z * DEG_TO_RAD);
        return m;
    }

    public static void rotate(Matrix4i m, Tuple3f angles) {
        m.mul(makeRotateMatrix(angles), m);
    }

    // angles in degrees
    public static Matrix4i makeRotateMatrix(Tuple3f angles) {
        return makeRotateMatrix(angles.x, angles.y, angles.z);
    }

    // angles in degrees
    public static Matrix4i makeRotateMatrix(float x, float y, float z) {
        Matrix4i m = new Matrix4i();
        m.setIdentity();
        rotX(m, x);
        rotY(m, y);;
        rotZ(m, z);
        return m;
    }

    public static Matrix4i makeRotateMatrix(Matrix3i rot) {
        Matrix4i m = new Matrix4i();
        m.m00 = rot.m00;
        m.m01 = rot.m01;
        m.m02 = rot.m02;
        m.m03 = 0;

        m.m10 = rot.m10;
        m.m11 = rot.m11;
        m.m12 = rot.m12;
        m.m13 = 0;

        m.m20 = rot.m20;
        m.m21 = rot.m21;
        m.m22 = rot.m22;
        m.m23 = 0;

        m.m30 = 0;
        m.m31 = 0;
        m.m32 = 0;
        m.m33 = 1;
        return m;
    }

    public static void rotX(Matrix4i m, float radians) {
        m.mul(makeRotXMatrix(radians), m);
    }

    public static Matrix4i makeRotXMatrix(float radians) {
        Matrix4i m = new Matrix4i();
        m.rotX(radians);
        return m;
    }

    public static void rotY(Matrix4i m, float radians) {
        m.mul(makeRotYMatrix(radians), m);
    }

    public static Matrix4i makeRotYMatrix(float radians) {
        Matrix4i m = new Matrix4i();
        m.rotY(radians);
        return m;
    }

    public static void rotZ(Matrix4i m, float radians) {
        m.mul(makeRotZMatrix(radians), m);
    }

    public static Matrix4i makeRotZMatrix(float radians) {
        Matrix4i m = new Matrix4i();
        m.rotZ(radians);
        return m;
    }

    public static void scale(Matrix4i m, int scale) {
        scale(m, new Point3i(scale, scale, scale));
    }

    public static void scale(Matrix4i m, Tuple3i scale) {
        m.mul(makeScaleMatrix(scale), m);
    }

    public static Matrix4i makeScaleMatrix(Tuple3i scale) {
        return makeScaleMatrix(scale.x, scale.y, scale.z);
    }

    public static Matrix4i makeScaleMatrix(int s) {
        return makeScaleMatrix(s, s, s);
    }

    public static Matrix4i makeScaleMatrix(int x, int y, int z) {
        Matrix4i m = new Matrix4i();
        m.m00 = x;
        m.m01 = 0;
        m.m02 = 0;
        m.m03 = 0;

        m.m10 = 0;
        m.m11 = y;
        m.m12 = 0;
        m.m13 = 0;

        m.m20 = 0;
        m.m21 = 0;
        m.m22 = z;
        m.m23 = 0;

        m.m30 = 0;
        m.m31 = 0;
        m.m32 = 0;
        m.m33 = 1;
        return m;
    }

    public static void translate(Matrix4i m, int x, int y, int z) {
        translate(m, new Point3i(x, y, z));
    }

    public static void translate(Matrix4i m, Tuple3i scale) {
        m.mul(makeTranslateMatrix(scale), m);
    }

    public static Matrix4i makeTranslateMatrix(Tuple3i translate) {
        Matrix4i m = new Matrix4i();
        m.m00 = 1;
        m.m01 = 0;
        m.m02 = 0;
        m.m03 = translate.x;

        m.m10 = 0;
        m.m11 = 1;
        m.m12 = 0;
        m.m13 = translate.y;

        m.m20 = 0;
        m.m21 = 0;
        m.m22 = 1;
        m.m23 = translate.z;

        m.m30 = 0;
        m.m31 = 0;
        m.m32 = 0;
        m.m33 = 1;
        return m;
    }

    public static void orientateTo(Matrix4i m, Tuple3f lookAt) {
        m.mul(makeOrientateToMatrix(lookAt), m);
    }

    // return transform that will rotate (1,0,0) to the direction vector given 
    public static Matrix4i makeOrientateToMatrix(Tuple3f lookAt) {
        Matrix4i m = new Matrix4i();
        m.setIdentity();
        boolean xZero = epsilonEquals(lookAt.x, 0);
        boolean yZero = epsilonEquals(lookAt.y, 0);
        boolean zZero = epsilonEquals(lookAt.z, 0);
        // evaluate trivial options
        if (xZero) {
            if (yZero) {
                if (zZero)
                    ; // no op
                else if (lookAt.z < 0) {
                    rotY(m, Euler90);
                } else {
                    rotY(m, -Euler90);
                }
            } else if (zZero) {
                if (lookAt.y < 0) {
                    rotZ(m, -Euler90);
                } else {
                    rotZ(m, Euler90);
                }
            } else {   // YZ plane
                float a = atan2(lookAt.y, lookAt.z);
                rotX(m, a);
                if (Math.signum(lookAt.y) == Math.signum(lookAt.z)) {
                    rotZ(m, Euler90);
                } else {
                    rotZ(m, -Euler90);
                }
            }
        } else if (yZero) {
            if (zZero) {
                if (lookAt.x < 0) {
                    rotY(m, Euler180);
                } else
                    ;
            } else {   // XZ plane
                float a = atan2(lookAt.z, lookAt.x);
                rotY(m, -a);
            }
        } else if (zZero) {   // XY plane
            float a = atan2(lookAt.y, lookAt.x);
            rotZ(m, a);
        } else {
            float xy = (float) Math.sqrt(lookAt.x * lookAt.x + lookAt.y * lookAt.y);
            rotZ(m, atan2(lookAt.y, lookAt.x));
            rotY(m, -atan2(lookAt.z, xy));
        }
        return m;
    }

    public static float[] toFloatArray(Matrix4i m) {
        float[] v = new float[16];
        v[ 0] = m.m00;
        v[ 1] = m.m10;
        v[ 2] = m.m20;
        v[ 3] = m.m30;

        v[ 4] = m.m01;
        v[ 5] = m.m11;
        v[ 6] = m.m21;
        v[ 7] = m.m31;

        v[ 8] = m.m02;
        v[ 9] = m.m12;
        v[10] = m.m22;
        v[11] = m.m32;

        v[12] = m.m03;
        v[13] = m.m13;
        v[14] = m.m23;
        v[15] = m.m33;

        return v;
    }

}
