package jo.vecmath.ext;

import jo.vecmath.Point3f;
import jo.vecmath.logic.Point3fLogic;

public class Plane3f {

    private Point3f mR; // position
    private Point3f mN; // normal

    public Plane3f() {
        mR = new Point3f();
        mN = new Point3f(0, 0, 1);
    }

    public Plane3f(Point3f r, Point3f n) {
        this();
        mR.set(r);
        mN.set(n);
        Point3fLogic.normalize(mN);
    }

    public Plane3f(Point3f p1, Point3f p2, Point3f p3) {
        this();
        mR.set(p1);
        Point3f u = Point3fLogic.sub(p2, p1);
        Point3f v = Point3fLogic.sub(p3, p1);
        mN.set(Point3fLogic.cross(u, v));
        Point3fLogic.normalize(mN);
    }

    public Plane3f(Point3f n, float radius) {
        this();
        mN.set(n);
        Point3fLogic.normalize(mN);
        mR.set(mN);
        mR.scale(radius);
    }

    public Plane3f(Plane3f p) {
        this(p.getR(), p.getN());
    }

    public String toString() {
        return mR + "||" + mN;
    }

    public float dist(Point3f p) {
        //return Math.abs(p.sub(mR).dot(mN));
        return (float) Math.abs(Point3fLogic.dot(Point3fLogic.sub(p, mR), mN));
    }

    public Point3f getR() {
        return mR;
    }

    public void setR(Point3f r) {
        mR = r;
    }

    public Point3f getN() {
        return mN;
    }

    public void setN(Point3f n) {
        mN = n;
    }
}
