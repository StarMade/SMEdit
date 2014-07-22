package jo.vecmath.ext;

import jo.vecmath.Point3f;
import jo.vecmath.logic.Point3fLogic;

public class Line3f {

    private Point3f mP;
    private Point3f mN;

    public Line3f() {
        mP = new Point3f();
        mN = new Point3f(0, 0, 1);
    }

    public Line3f(Point3f p, Point3f n) {
        this();
        mP.set(p);
        mN.set(n);
        Point3fLogic.normalize(mN);
    }

    public Line3f(Line3f l) {
        this(l.getP(), l.getN());
    }

    public String toString() {
        return mP.toString() + "--" + mN.toString();
    }

    public double dist(Point3f m) {
        Point3f direct = Point3fLogic.sub(m, mP);
        Point3f projected = Point3fLogic.scale(mN, Point3fLogic.dot(direct, mN));
        double d = Point3fLogic.mag(Point3fLogic.sub(direct, projected));
        return d;
    }

    public Point3f getP() {
        return mP;
    }

    public void setP(Point3f p) {
        mP = p;
    }

    public Point3f getN() {
        return mN;
    }

    public void setN(Point3f n) {
        mN = n;
    }
}
