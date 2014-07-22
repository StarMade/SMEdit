package jo.vecmath.logic.ext;

import jo.vecmath.Point3f;
import jo.vecmath.ext.Line3f;
import jo.vecmath.ext.LineSegment3f;
import jo.vecmath.logic.Point3fLogic;

public class Line3fLogic {

    // make a line from two points
    public static Line3f fromPoints(Point3f p1, Point3f p2) {
        return new Line3f(p1, Point3fLogic.sub(p2, p1));
    }

    public static Point3f[] closestPoints(Line3f l1, Line3f l2) {
        Point3f p21 = Point3fLogic.sub(l2.getP(), l1.getP());
        Point3f m = Point3fLogic.cross(l2.getN(), l1.getN());
        float m2 = Point3fLogic.dot(m, m);
        if (m2 == 0) {
            return null;
        }
        Point3f r = Point3fLogic.cross(p21, Point3fLogic.scale(m, 1 / m2));
        float t1 = Point3fLogic.dot(r, l2.getN());
        Point3f q1 = Point3fLogic.add(l1.getP(), Point3fLogic.scale(l1.getN(), t1));
        float t2 = Point3fLogic.dot(r, l1.getN());
        Point3f q2 = Point3fLogic.add(l2.getP(), Point3fLogic.scale(l2.getN(), t2));
        return new Point3f[]{q1, q2};
    }

    public static float dist(Line3f l, Point3f p) {
        Point3f direct = Point3fLogic.sub(p, l.getP());
        Point3f projected = Point3fLogic.scale(l.getN(), Point3fLogic.dot(direct, l.getN()));
        float d = Point3fLogic.mag(Point3fLogic.sub(direct, projected));
        return d;
    }

    public static float dist(Line3f l1, Line3f l2) {
        Point3f[] closest = closestPoints(l1, l2);
        if (closest == null) {
            return dist(l1, l2.getP()); // parallel
        }
        float dist = closest[0].distance(closest[1]);
        return dist;
    }

    public static Point3f intersect(Line3f l1, Line3f l2) {
        Point3f[] closest = closestPoints(l1, l2);
        if (closest == null) {
            return null;
        }
        double dist = closest[0].distance(closest[1]);
        if (dist > Point3fLogic.EPSILON) {
            return null;
        }
        return closest[0];
    }

    public static Line3f fromSegment(LineSegment3f segment) {
        return fromPoints(segment.getP1(), segment.getP2());
    }
}
