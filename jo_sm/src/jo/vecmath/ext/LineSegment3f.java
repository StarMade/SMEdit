package jo.vecmath.ext;

import jo.vecmath.Point3f;

public class LineSegment3f {

    private Point3f mP1;
    private Point3f mP2;

    public LineSegment3f() {
        mP1 = new Point3f();
        mP2 = new Point3f(0, 0, 1);
    }

    public LineSegment3f(Point3f p1, Point3f p2) {
        this();
        mP1.set(p1);
        mP2.set(p2);
    }

    public LineSegment3f(LineSegment3f l) {
        this(l.getP1(), l.getP2());
    }

    public String toString() {
        return mP1.toString() + "--" + mP2.toString();
    }

    public double length() {
        return mP1.distance(mP2);
    }

    public Point3f getP1() {
        return mP1;
    }

    public void setP1(Point3f p1) {
        mP1 = p1;
    }

    public Point3f getP2() {
        return mP2;
    }

    public void setP2(Point3f p2) {
        mP2 = p2;
    }
}
