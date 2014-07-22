package jo.vecmath.logic;

import jo.vecmath.Matrix3i;
import jo.vecmath.Matrix4d;
import jo.vecmath.Matrix4i;
import jo.vecmath.Point3i;
import jo.vecmath.Point4i;
import jo.vecmath.Tuple3f;
import jo.vecmath.Tuple3i;

public class TransformInteger extends Matrix4i {

    /**
     *
     */
    private static final long serialVersionUID = -6216638219578138997L;

    public TransformInteger() {
        super();
    }

    public TransformInteger(int m00, int m01, int m02, int m03, int m10,
            int m11, int m12, int m13, int m20, int m21, int m22,
            int m23, int m30, int m31, int m32, int m33) {
        super(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31,
                m32, m33);
    }

    public TransformInteger(int[] v) {
        super(v);
    }

    public TransformInteger(Matrix3i m1, Point3i t1, int s) {
        super(m1, t1, s);
    }

    public TransformInteger(Matrix4d m1) {
        super(m1);
    }

    public TransformInteger(Matrix4i m1) {
        super(m1);
    }

    public TransformInteger(Point4i q1, Point3i t1, int s) {
        super(q1, t1, s);
    }

    public void prepend(Matrix4i m) {
        mul(m, this);
    }

    public void append(Matrix4i m) {
        mul(this, m);
    }

    public void rotate(Tuple3f angles) {
        prepend(Matrix4iLogic.makeRotateMatrix(angles));
    }

    public void rotate(float xRadians, float yRadians, float zRadians) {
        prepend(Matrix4iLogic.makeRotateMatrix(xRadians, yRadians, zRadians));
    }

    public void rotateX(float aRadians) {
        prepend(Matrix4iLogic.makeRotXMatrix(aRadians));
    }

    public void rotateY(float aRadians) {
        prepend(Matrix4iLogic.makeRotYMatrix(aRadians));
    }

    public void rotateZ(float aRadians) {
        prepend(Matrix4iLogic.makeRotZMatrix(aRadians));
    }

    public void rotateEuler(float xEuler, float yEuler, float zEuler) {
        rotate(xEuler * MathUtils.DEG_TO_RAD, yEuler * MathUtils.DEG_TO_RAD, zEuler * MathUtils.DEG_TO_RAD);
    }

    public void rotateXEuler(float aEuler) {
        rotateX(aEuler * MathUtils.DEG_TO_RAD);
    }

    public void rotateYEuler(float aEuler) {
        rotateY(aEuler * MathUtils.DEG_TO_RAD);
    }

    public void rotateZEuler(float aEuler) {
        rotateZ(aEuler * MathUtils.DEG_TO_RAD);
    }

    public void rotate(Matrix3i rot) {
        prepend(Matrix4iLogic.makeRotateMatrix(rot));
    }

    public void scale(Tuple3i scale) {
        prepend(Matrix4iLogic.makeScaleMatrix(scale.x, scale.y, scale.z));
    }

    public void scale(int s) {
        prepend(Matrix4iLogic.makeScaleMatrix(s, s, s));
    }

    public void scale(int x, int y, int z) {
        prepend(Matrix4iLogic.makeScaleMatrix(x, y, z));
    }

    public void translate(int x, int y, int z) {
        prepend(Matrix4iLogic.makeTranslateMatrix(new Point3i(x, y, z)));
    }

    public void translate(Tuple3i scale) {
        prepend(Matrix4iLogic.makeTranslateMatrix(scale));
    }

    public Point3i getForward() {
        Point3i fwd = new Point3i(0, 0, 1);
        Matrix3i rot = new Matrix3i();
        get(rot);
        rot.transform(fwd);
        return fwd;
    }

    public Point3i getUp() {
        Point3i up = new Point3i(0, 1, 0);
        Matrix3i rot = new Matrix3i();
        get(rot);
        rot.transform(up);
        return up;
    }

    public Point3i getRight() {
        Point3i right = new Point3i(1, 0, 0);
        Matrix3i rot = new Matrix3i();
        get(rot);
        rot.transform(right);
        return right;
    }

    public void advance(int dist) {
        Point3i fwd = getForward();
        fwd.scale(dist);
        translate(fwd);
    }

    public void rotateAround(Tuple3i around, Tuple3f rot) {
        translate(around);
        rotate(rot);
        translate(-around.x, -around.y, -around.z);
    }
}
