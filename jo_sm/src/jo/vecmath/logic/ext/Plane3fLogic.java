package jo.vecmath.logic.ext;

import jo.vecmath.Point3f;
import jo.vecmath.ext.Line3f;
import jo.vecmath.ext.Plane3f;
import jo.vecmath.logic.Point3fLogic;

public class Plane3fLogic {

    // make a plane from a point, and two vectors originating at that point
    public static Plane3f fromPointAndVectors(Point3f p, Point3f v1, Point3f v2) {
        Point3f norm = Point3fLogic.cross(v1, v2);
        return new Plane3f(p, norm);
    }

    // make a plane from three points
    public static Plane3f fromPoints(Point3f p1, Point3f p2, Point3f p3) {
        return fromPointAndVectors(p1, Point3fLogic.sub(p2, p1), Point3fLogic.sub(p3, p1));
    }

    public static Line3f intersect(Plane3f p1, Plane3f p2) {
        float h1 = Point3fLogic.dot(p1.getR(), p1.getN());
        float h2 = Point3fLogic.dot(p2.getR(), p2.getN());
        float n1dotn2 = Point3fLogic.dot(p1.getN(), p2.getN());
        if (Math.abs(Math.abs(n1dotn2) - 1) < Point3fLogic.EPSILON) {
            return null;
        }
        float c1 = (h1 - h2 * n1dotn2) / (1 - n1dotn2 * n1dotn2);
        float c2 = (h2 - h1 * n1dotn2) / (1 - n1dotn2 * n1dotn2);
        Point3f p = Point3fLogic.add(Point3fLogic.scale(p1.getN(), c1), Point3fLogic.scale(p2.getN(), c2));
        Point3f n = Point3fLogic.cross(p1.getN(), p2.getN());
        return new Line3f(p, n);
    }

    public static float angle(Plane3f p1, Plane3f p2) {
        float cosa = Point3fLogic.dot(p1.getN(), p2.getN());
        return (float) Math.acos(cosa);
    }

    public static int whichSide(Plane3f plane, Point3f point) {
        float dot = Point3fLogic.dot(Point3fLogic.sub(point, plane.getR()), plane.getN());
        return Point3fLogic.sgn(dot);
    }

    // http://geomalgorithms.com/a06-_intersect-2.html
    public static Point3f intersection(Plane3f plane, Line3f line) {
        float d = Point3fLogic.dot(plane.getN(), line.getN());
        if (Point3fLogic.isZero(d)) {
            return null; // parallel
        }
        float n = Point3fLogic.dot(plane.getN(), Point3fLogic.sub(plane.getR(), line.getP()));
        float r = n / d;
        Point3f i = Point3fLogic.add(line.getP(), Point3fLogic.scale(line.getN(), r));
        return i;
    }
}
